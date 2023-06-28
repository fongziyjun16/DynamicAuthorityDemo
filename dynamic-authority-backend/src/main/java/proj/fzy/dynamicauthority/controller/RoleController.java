package proj.fzy.dynamicauthority.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import proj.fzy.dynamicauthority.model.db.Authority;
import proj.fzy.dynamicauthority.model.db.Role;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;
import proj.fzy.dynamicauthority.service.RoleService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> create(@RequestParam String name) {
        return roleService.createNewRole(name) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "duplicate role");
    }

    @DeleteMapping("/{name}")
    public CommonResponse<Void> delete(@PathVariable String name) {
        return roleService.deleteRole(name) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.NOT_FOUND.value(), "role not exists");
    }

    @GetMapping("/{name}")
    public CommonResponse<Role> getByName(@PathVariable String name) {
        Role dbRole = roleService.getByName(name);
        return dbRole == null ?
                CommonResponse.<Role>builder().code(HttpStatus.NOT_FOUND.value()).message("role not exists").build() :
                CommonResponse.<Role>builder().code(HttpStatus.OK.value()).message("success").data(dbRole).build();
    }

    @GetMapping("/all")
    public CommonResponse<List<Role>> getAll() {
        return CommonResponse.<List<Role>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(roleService.getAll())
                .build();
    }

    @GetMapping("/authority/{roleName}")
    public CommonResponse<Set<Authority>> getAuthorities(@PathVariable String roleName) {
        return CommonResponse.<Set<Authority>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(roleService.getAuthorities(roleName))
                .build();
    }

    @PostMapping(value = "/authority", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> addAuthority(@RequestParam String roleName, @RequestParam String authorityName) {
        return roleService.addAuthority(roleName, authorityName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "role or authority not exist, or role already has authority");
    }

    @DeleteMapping(value = "/authority")
    public CommonResponse<Void> removeAuthority(@RequestParam String roleName, @RequestParam String authorityName) {
        return roleService.removeAuthority(roleName, authorityName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "role or authority not exist, or role does not have this authority");
    }
}
