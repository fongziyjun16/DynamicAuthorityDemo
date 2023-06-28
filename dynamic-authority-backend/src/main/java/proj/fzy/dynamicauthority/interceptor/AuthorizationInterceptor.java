package proj.fzy.dynamicauthority.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import proj.fzy.dynamicauthority.enums.ResourceAuthorizationType;
import proj.fzy.dynamicauthority.exception.AuthorizationException;
import proj.fzy.dynamicauthority.exception.NotFoundException;
import proj.fzy.dynamicauthority.model.db.Resource;
import proj.fzy.dynamicauthority.model.db.User;
import proj.fzy.dynamicauthority.repository.ResourceRepository;
import proj.fzy.dynamicauthority.repository.UserRepository;
import proj.fzy.dynamicauthority.untils.AuthorizationUtils;
import proj.fzy.dynamicauthority.untils.JWTUtils;

import java.util.*;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JWTUtils jwtUtils;
    private final InterceptorUtils interceptorUtils;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public AuthorizationInterceptor(
            JWTUtils jwtUtils, InterceptorUtils interceptorUtils,
            UserRepository userRepository, ResourceRepository resourceRepository) {
        this.jwtUtils = jwtUtils;
        this.interceptorUtils = interceptorUtils;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (interceptorUtils.isPermitAll(request)) {
            return true;
        }
        // retrieve db resource
        Optional<Resource> optionalResource = resourceRepository.findByMethodAndPath(
                request.getMethod(),
                (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        if (optionalResource.isEmpty()) {
            throw new NotFoundException();
        }
        Resource dbResource = optionalResource.get();
        // check whether resource need authorization
        if (dbResource.getAuthorizationType() == ResourceAuthorizationType.NO_AUTH) {
            return true;
        }
        // extract token
        String token = jwtUtils.extraTokenFromHttpServletRequest(request);
        String username = jwtUtils.getUsername(token);
        // retrieve db user
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new AuthorizationException();
        }
        User dbUser = optionalUser.get();
        // authority verification
        Set<String> dbUserTags = AuthorizationUtils.getTags(dbUser.getRoles(), dbUser.getAuthorities());
        Set<String> dbResourceTags = AuthorizationUtils.getTags(dbResource.getRoles(), dbResource.getAuthorities());
        return dbResource.getAuthorizationType() == ResourceAuthorizationType.ANY ?
                AuthorizationUtils.hasAny(dbUserTags, dbResourceTags) :
                AuthorizationUtils.hasAll(dbUserTags, dbResourceTags);
    }
}
