package com.co.eatupapi.utils.commercial.seller.security;

import com.co.eatupapi.config.user.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class SellerSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SellerSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                                ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    @Order(0)
    @SuppressWarnings({"java:S112", "java:S1130"}) // Spring Security API requires throws Exception
    public SecurityFilterChain sellerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/comercialapi/v1/sellers/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                writeSecurityErrorResponse(
                                        response,
                                        HttpStatus.UNAUTHORIZED,
                                        "Token requerido para acceder a este recurso",
                                        "SELLER_UNAUTHORIZED"
                                )
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeSecurityErrorResponse(
                                        response,
                                        HttpStatus.FORBIDDEN,
                                        "No tienes permisos para realizar esta acción",
                                        "SELLER_FORBIDDEN"
                                )
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void writeSecurityErrorResponse(HttpServletResponse response,
                                            HttpStatus status,
                                            String message,
                                            String errorCode) throws java.io.IOException {
        if (response.isCommitted()) return;

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("errorCode", errorCode);
        body.put("status", status.value());

        objectMapper.writeValue(response.getWriter(), body);
    }
}
