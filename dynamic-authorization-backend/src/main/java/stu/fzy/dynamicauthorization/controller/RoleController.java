package stu.fzy.dynamicauthorization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;
import stu.fzy.dynamicauthorization.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public CommonResponse<Void> create(@RequestParam String name) {
        return roleService.create(name) ?
                CommonResponse.simpleSuccess() :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "duplicate role");
    }

    @DeleteMapping("/{name}")
    public CommonResponse<Void> delete(@PathVariable String name) {
        return roleService.delete(name) ?
                CommonResponse.simpleSuccess() :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "no role");
    }

    @GetMapping("/{name}")
    public CommonResponse<Role> get(@PathVariable String name) {
        Role role = roleService.get(name);
        return role != null ?
                CommonResponse.successWithData(role) :
                CommonResponse.<Role>builder().code(HttpStatus.NOT_FOUND.value()).message("Not Found").build();
    }

    @GetMapping("/all")
    public CommonResponse<List<Role>> getAll() {
        return CommonResponse.successWithData(roleService.getAll());
    }

}
