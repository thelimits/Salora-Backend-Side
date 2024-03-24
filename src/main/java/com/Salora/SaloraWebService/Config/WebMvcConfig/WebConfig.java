package com.Salora.SaloraWebService.Config.WebMvcConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebConfig {
    @Value("${cors.allowed.origin}")
    private String[] allowedOrigins;

    public WebConfig(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.POST.name(),
                HttpMethod.GET.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name()
        ));
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
