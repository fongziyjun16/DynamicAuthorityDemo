package stu.fzy.dynamicauthorization.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import stu.fzy.dynamicauthorization.enums.ResourceAuthType;
import stu.fzy.dynamicauthorization.model.db.Resource;
import stu.fzy.dynamicauthorization.model.db.Role;
import stu.fzy.dynamicauthorization.service.ResourceService;
import stu.fzy.dynamicauthorization.utils.EndpointUtils;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class AppAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final EndpointUtils endpointUtils;
    private final ResourceService resourceService;

    public AppAuthorizationManager(EndpointUtils endpointUtils, ResourceService resourceService) {
        this.endpointUtils = endpointUtils;
        this.resourceService = resourceService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        boolean granted = true;
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        if (!endpointUtils.isPermitAll(request)) {
            granted = false;
            Resource resource = resourceService.getResource(request.getMethod(), endpointUtils.getRequestPattern(request));
            if (resource != null) {
                if (!resource.getAuthType().equals(ResourceAuthType.JUST_AUTHENTICATION)) {
                    Set<String> userRoles = authentication.get().getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());
                    Set<String> resourceRoles = resource.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                    if (resource.getAuthType().equals(ResourceAuthType.ANY_ROLE)) {
                        for (String resourceRole : resourceRoles) {
                            if (userRoles.contains(resourceRole)) {
                                granted = true;
                                break;
                            }
                        }
                    } else { // ResourceAuthType.ALL_ROLE
                        granted = userRoles.containsAll(resourceRoles);
                    }
                } else {
                    granted = true;
                }
            }
        }
        return new AuthorizationDecision(granted);
    }
}
