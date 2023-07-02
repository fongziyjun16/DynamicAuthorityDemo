package stu.fzy.dynamicauthorization.filter;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import stu.fzy.dynamicauthorization.exception.AuthenticationException;
import stu.fzy.dynamicauthorization.model.AuthenticatedUser;
import stu.fzy.dynamicauthorization.model.db.User;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;
import stu.fzy.dynamicauthorization.repository.UserRepository;
import stu.fzy.dynamicauthorization.utils.EndpointUtils;
import stu.fzy.dynamicauthorization.utils.JWTUtils;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    private final EndpointUtils endpointUtils;

    public JWTFilter(JWTUtils jwtUtils, UserRepository userRepository, EndpointUtils endpointUtils) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.endpointUtils = endpointUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return endpointUtils.isPermitAll(request);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        User dbUser = null;
        String token = jwtUtils.extraTokenFromHttpServletRequest(request);
        if (token != null && jwtUtils.verify(token) &&
                (dbUser = userRepository.findByUsername(jwtUtils.getUsername(token)).orElse(null)) != null) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(dbUser);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } else {
            AuthenticationException authenticationException = new AuthenticationException();
            response.setContentType("application/json");
            response.getWriter().write(
                    JSONUtil.toJsonStr(CommonResponse.simpleResponse(authenticationException.getCode(), authenticationException.getMessage())));
        }
    }
}
