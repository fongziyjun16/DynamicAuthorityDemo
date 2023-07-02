package stu.fzy.dynamicauthorization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;
import stu.fzy.dynamicauthorization.model.dto.RoleAssignment;
import stu.fzy.dynamicauthorization.service.ResourceService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public CommonResponse<Resource> get(@RequestParam String method, @RequestParam String path) {
        Resource resource = resourceService.getResource(method, path);
        return resource != null ?
                CommonResponse.successWithData(resource) :
                CommonResponse.<Resource>builder().code(HttpStatus.BAD_REQUEST.value()).message("wrong resource").build();
    }

    @GetMapping("/all")
    public CommonResponse<List<Resource>> getAll() {
        return CommonResponse.successWithData(resourceService.getAll());
    }

    @PostMapping(value = "/role", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public CommonResponse<Void> assignRole(@RequestBody RoleAssignment roleAssignment) {
        return resourceService.assignRole(
                roleAssignment.getMethod(), roleAssignment.getPath(),
                roleAssignment.getAuthType(), roleAssignment.getRoleNames()) ?
                CommonResponse.simpleSuccess() :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "wrong endpoint or roles, or endpoint has had role");
    }

    @GetMapping(value = "/role")
    public CommonResponse<Set<Role>> getRoles(@RequestParam String method, @RequestParam String path) {
        return CommonResponse.successWithData(resourceService.getRoles(method, path));
    }

}
