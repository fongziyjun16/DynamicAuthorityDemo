package stu.fzy.dynamicauthorization.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import stu.fzy.dynamicauthorization.annotation.PermitAll;
import stu.fzy.dynamicauthorization.exception.AuthenticationException;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.model.db.User;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;
import stu.fzy.dynamicauthorization.model.dto.RoleAssignment;
import stu.fzy.dynamicauthorization.service.UserService;
import stu.fzy.dynamicauthorization.utils.JWTUtils;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    public UserController(JWTUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> signUp(@RequestParam String username, @RequestParam String password) {
        return userService.signUp(username, password) ?
                CommonResponse.simpleSuccess() :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "duplicate user");
    }

    @PermitAll
    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<String> signIn(@RequestParam String username, @RequestParam String password,
                                         HttpServletRequest request, HttpServletResponse response) {
        String token = userService.signIn(username, password);
        if (token != null) {
            response.setHeader(
                    HttpHeaders.SET_COOKIE,
                    ResponseCookie.from("token", token)
                            .httpOnly(true)
                            .secure(false)
                            .domain(request.getServerName())
                            .path("/")
                            .sameSite("Lax")
                            .build()
                            .toString()
            );
            return CommonResponse.successWithData(token);
        }
        throw new AuthenticationException("wrong username or password");
    }

    @GetMapping
    public CommonResponse<User> get(@RequestParam String username) {
        User user = userService.get(username);
        return user != null ?
                CommonResponse.successWithData(user) :
                CommonResponse.<User>builder().code(HttpStatus.BAD_REQUEST.value()).message("wrong resource").build();
    }

    @GetMapping("/all")
    public CommonResponse<List<User>> getAll() {
        return CommonResponse.successWithData(userService.getAll());
    }

    @GetMapping("/role")
    public CommonResponse<Set<Role>> getRoles(HttpServletRequest request) {
        return CommonResponse.successWithData(userService.getRoles(jwtUtils.getUsername(jwtUtils.extraTokenFromHttpServletRequest(request))));
    }

    @PostMapping(value = "/role", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<Void> assignRoles(@RequestBody RoleAssignment roleAssignment) {
        return userService.assignRole(
                roleAssignment.getUsername(), roleAssignment.getRoleNames()) ?
                CommonResponse.simpleSuccess() :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "wrong endpoint or roles, or endpoint has had role");
    }

}
