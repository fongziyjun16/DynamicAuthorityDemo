package proj.fzy.dynamicauthority.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import proj.fzy.dynamicauthority.interceptor.AuthorizationInterceptor;
import proj.fzy.dynamicauthority.interceptor.AuthenticationInterceptor;

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
        registry.addInterceptor(authenticationInterceptor);
        registry.addInterceptor(authorizationInterceptor);
    }
}
