package com.co.eatupapi.config.payment.cashreceipt;

import com.co.eatupapi.config.user.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class CashReceiptSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public CashReceiptSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain cashReceiptSecurityFilterChain(HttpSecurity http) throws ServletException {
        try {
            http
                    .securityMatcher("/api/v1/locations/*/cashreceipts/**")
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((request, response, authException) ->
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                            )
                    )
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter.class);
            return http.build();
        } catch (RuntimeException e) {
            throw new ServletException("Error configuring cash receipt security", e);
        }
    }
}