
package com.sonidovivo.pedidosservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configurarOpenAPI() {

        String esquema = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Sonido Vivo - Pedidos Service")
                        .version("1.0.0")
                        .description(
                                "API REST para la gestión de pedidos " +
                                "y sus productos en Sonido Vivo."
                        )
                )
                .addSecurityItem(
                        new SecurityRequirement().addList(esquema)
                )
                .components(
                        new Components().addSecuritySchemes(
                                esquema,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
