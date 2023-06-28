package proj.fzy.dynamicauthority.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import proj.fzy.dynamicauthority.exception.NotFoundException;
import proj.fzy.dynamicauthority.model.dto.CommonResponse;
import proj.fzy.dynamicauthority.model.dto.ResourceInfo;
import proj.fzy.dynamicauthority.service.ResourceService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/endpoints")
    public CommonResponse<Map<String, Set<String>>> getEndpointInfo() {
        return CommonResponse.<Map<String, Set<String>>>builder()
                .code(200)
                .message("success")
                .data(resourceService.getEndpointInfo())
                .build();
    }

    @GetMapping
    public CommonResponse<ResourceInfo> get(@RequestParam String method, @RequestParam String path) {
        ResourceInfo resourceInfo = resourceService.get(method, path);
        if (resourceInfo == null) {
            throw new NotFoundException();
        }
        return CommonResponse.<ResourceInfo>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(resourceInfo)
                .build();
    }

    @GetMapping("/all")
    public CommonResponse<List<ResourceInfo>> getAll() {
        return CommonResponse.<List<ResourceInfo>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(resourceService.getAll())
                .build();
    }

    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> assignRole(@RequestParam String method, @RequestParam String path, @RequestParam String roleName) {
        return resourceService.assignRole(method, path, roleName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "resource or role not exist");
    }

    @DeleteMapping("/role")
    public CommonResponse<Void> removeRole(@RequestParam String method, @RequestParam String path, @RequestParam String roleName) {
        return resourceService.removeRole(method, path, roleName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "resource or role not exist");
    }

    @PostMapping(value = "/authority", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResponse<Void> assignAuthority(@RequestParam String method, @RequestParam String path, @RequestParam String authorityName) {
        return resourceService.assignAuthority(method, path, authorityName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "resource or authority not exist");
    }

    @DeleteMapping("/authority")
    public CommonResponse<Void> removeAuthority(@RequestParam String method, @RequestParam String path, @RequestParam String authorityName) {
        return resourceService.removeAuthority(method, path, authorityName) ?
                CommonResponse.simpleResponse(HttpStatus.OK.value(), "success") :
                CommonResponse.simpleResponse(HttpStatus.BAD_REQUEST.value(), "resource or authority not exist");
    }
}
