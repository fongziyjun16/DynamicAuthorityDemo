package stu.fzy.dynamicauthorization.component;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.service.ResourceService;
import stu.fzy.dynamicauthorization.service.RoleService;
import stu.fzy.dynamicauthorization.service.UserService;
import stu.fzy.dynamicauthorization.utils.EndpointUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {

    private final UserService userService;
    private final RoleService roleService;
    private final ResourceService resourceService;
    private final EndpointUtils endpointUtils;

    public ApplicationStartedEventHandler(
            UserService userService, RoleService roleService, ResourceService resourceService,
            EndpointUtils endpointUtils) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.endpointUtils = endpointUtils;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        initialUser();
        initialRole();
        userService.assignRole("default", List.of("root"));
        storeResources();
        setEndpointPermitAll();
    }

    private void initialUser() {
        userService.signUp("default", "123qweasd");
        userService.signUp("tommy123", "123qweasd");
    }

    private void initialRole() {
        roleService.create("root");
    }

    private void storeResources() {
        Map<String, Set<String>> appEndpoints = endpointUtils.getAppEndpoints();
        Map<String, Set<String>> dbEndpoints = resourceService.getDBEndpoints();
        Map<String, Set<String>> difference = endpointUtils.differenceFromLeft(appEndpoints, dbEndpoints);
        if (!difference.isEmpty()) {
            difference.forEach((method, paths) -> {
                paths.forEach(path ->
                        resourceService.create(method, path, ResourceAuthType.JUST_AUTHENTICATION));
            });
        }
    }

    private void setEndpointPermitAll() {
        Map<String, Set<String>> appEndpoints = endpointUtils.getAppEndpoints();
        Map<String, Set<String>> endpointsWithPermitAll = endpointUtils.getEndpointsWithPermitAll();
        appEndpoints.forEach((method, paths) -> {
            paths.forEach(path -> {
                Resource resource = resourceService.getResource(method, path);
                if (resource != null) {
                    if (endpointsWithPermitAll.containsKey(method) && endpointsWithPermitAll.get(method).contains(path)) {
                        resourceService.updateAuthType(method, path, ResourceAuthType.PERMIT_ALL);
                        resourceService.clearRoles(method, path);
                    } else {
                        if (resource.getAuthType() == ResourceAuthType.PERMIT_ALL) {
                            resourceService.updateAuthType(method, path, ResourceAuthType.JUST_AUTHENTICATION);
                        }
                    }
                }
            });
        });
    }
}
