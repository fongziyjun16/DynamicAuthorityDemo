package proj.fzy.dynamicauthority.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import proj.fzy.dynamicauthority.exception.AuthenticationException;
import proj.fzy.dynamicauthority.untils.JWTUtils;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JWTUtils jwtUtils;
    private final InterceptorUtils interceptorUtils;

    public AuthenticationInterceptor(JWTUtils jwtUtils, InterceptorUtils interceptorUtils) {
        this.jwtUtils = jwtUtils;
        this.interceptorUtils = interceptorUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!interceptorUtils.isPermitAll(request)) {
            String token = jwtUtils.extraTokenFromHttpServletRequest(request);
            if (token != null && jwtUtils.verify(token)) {
                return true;
            }
            throw new AuthenticationException();
        }
        return true;
    }
}
