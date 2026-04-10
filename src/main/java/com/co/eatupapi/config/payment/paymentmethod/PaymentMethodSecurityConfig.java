package com.co.eatupapi.config.payment.paymentmethod;

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
public class PaymentMethodSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public PaymentMethodSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(4)
    public SecurityFilterChain paymentMethodSecurityFilterChain(HttpSecurity http) throws ServletException {
        try {
            http
                    .securityMatcher("/api/v1/payment-methods/**")
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
            throw new ServletException("Error configuring payment method security", e);
        }
    }
}
