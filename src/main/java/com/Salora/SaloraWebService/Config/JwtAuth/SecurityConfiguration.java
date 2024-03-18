package com.Salora.SaloraWebService.Config.JwtAuth;

import com.Salora.SaloraWebService.Security.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.Salora.SaloraWebService.Security.Permission.ADMIN_CREATE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req ->
                                req.requestMatchers(WHITE_LIST_URL).permitAll()
                                        .requestMatchers("/products/**").permitAll()
                                        .requestMatchers("/account/**").permitAll()
                                        .requestMatchers(POST ,"/products").hasAnyRole(RolePermission.ADMIN.name())
                                        .requestMatchers(POST ,"/products").hasAnyAuthority(ADMIN_CREATE.name())
                                        .requestMatchers(POST, "/api/v1/auth/validate", "/api/v1/auth/session").hasAnyRole(RolePermission.ADMIN.name(), RolePermission.CUSTOMER.name())
                                        .requestMatchers("/account/profile").hasAnyRole(RolePermission.ADMIN.name(), RolePermission.CUSTOMER.name())
                                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                    logout ->
                            logout.logoutUrl("/api/v1/auth/logout")
                                    .addLogoutHandler(logoutHandler)
                                    .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                );

        return http.build();
    }

}
