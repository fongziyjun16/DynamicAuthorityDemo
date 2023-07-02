package stu.fzy.dynamicauthorization.config;

import cn.hutool.json.JSONUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import stu.fzy.dynamicauthorization.component.AppAuthorizationManager;
import stu.fzy.dynamicauthorization.exception.AuthenticationException;
import stu.fzy.dynamicauthorization.filter.JWTFilter;
import stu.fzy.dynamicauthorization.model.dto.CommonResponse;

@Component
@EnableWebSecurity
public class WebSecurityConfig {

    private final JWTFilter jwtFilter;
    private final AppAuthorizationManager appAuthorizationManager;

    public WebSecurityConfig(JWTFilter jwtFilter, AppAuthorizationManager appAuthorizationManager) {
        this.jwtFilter = jwtFilter;
        this.appAuthorizationManager = appAuthorizationManager;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().access(appAuthorizationManager))
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                                response.setContentType("application/json");
                                response.getWriter().write(
                                        JSONUtil.toJsonStr(
                                                CommonResponse.simpleResponse(HttpStatus.FORBIDDEN.value(), "Authorization Failure")));
                            })
                            .authenticationEntryPoint(((request, response, authException) -> {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setContentType("application/json");
                                response.getWriter().write(
                                        JSONUtil.toJsonStr(
                                                CommonResponse.simpleResponse(HttpStatus.UNAUTHORIZED.value(), "Authentication Failure")));
                            }));
                });
        return http.build();
    }

}
