package com.co.eatupapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "EatUp API",
                version = "1.0",
                description = "Sistema para gestión de restaurante la curva del gordo"
        )
)
public class SwaggerConfig {
}