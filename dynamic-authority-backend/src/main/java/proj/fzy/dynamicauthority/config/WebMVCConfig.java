package proj.fzy.dynamicauthority.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import proj.fzy.dynamicauthority.interceptor.AuthorizationInterceptor;
import proj.fzy.dynamicauthority.interceptor.AuthenticationInterceptor;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMVCConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebMVCConfig(AuthenticationInterceptor authenticationInterceptor, AuthorizationInterceptor authorizationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclude = List.of(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
        );
        registry.addInterceptor(authenticationInterceptor)
                .excludePathPatterns(exclude);
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(exclude);
    }
}
