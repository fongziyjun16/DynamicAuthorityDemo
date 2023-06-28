package proj.fzy.dynamicauthority.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import proj.fzy.dynamicauthority.exception.AuthenticationException;
import proj.fzy.dynamicauthority.exception.NotFoundException;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;
import proj.fzy.dynamicauthority.model.dto.LoginResponse;
import proj.fzy.dynamicauthority.model.dto.UserInfo;
import proj.fzy.dynamicauthority.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<LoginResponse> signIn(@RequestParam String username, @RequestParam String password,
                                                HttpServletRequest request, HttpServletResponse response) {
        String token = userService.signIn(username, password);
        if (token == null) {
            return  CommonResponse.<LoginResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("wrong username or password")
                    .data(null)
                    .build();
        }
        response.setHeader(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from("token", token)
                        .httpOnly(true)
                        .secure(false)
                        .domain(request.getServerName())
                        .path("/")
                        .sameSite("Lax")
                        .build().toString()
        );
        boolean isRoot = userService.hasRoot(username);
        return CommonResponse.<LoginResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .data(LoginResponse.builder().token(token).hasRoot(isRoot).build())
                        .build();
    }

    @GetMapping("/authenticate")
    public CommonResponse<Void> authenticate(HttpServletRequest request) {
        if (userService.authenticate(request)) {
            return CommonResponse.simpleResponse(HttpStatus.OK.value(), "success");
        }
        throw new AuthenticationException();
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> signUp(@RequestParam String username, @RequestParam String password) {
        return userService.signUp(username, password) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "duplicate username");
    }

    @GetMapping("/{username}")
    public CommonResponse<UserInfo> getUserInfo(@PathVariable String username) {
        UserInfo userInfo = userService.getUserInfo(username);
        if (userInfo != null) {
            return CommonResponse.<UserInfo>builder()
                    .code(HttpStatus.OK.value())
                    .message("success")
                    .data(userInfo)
                    .build();
        }
        throw new NotFoundException();
    }

    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> addRole(@RequestParam String username, @RequestParam String roleName) {
        return userService.addRole(username, roleName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "not exist user or role");
    }

    @PostMapping(value = "/authority", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> addAuthority(@RequestParam String username, @RequestParam String authorityName) {
        return userService.addAuthority(username, authorityName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "not exist user or authority");
    }

}
