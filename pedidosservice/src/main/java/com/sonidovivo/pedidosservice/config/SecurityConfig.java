package com.sonidovivo.pedidosservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtFilter) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Solicitudes CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Documentación Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()

                        // El cliente ve su historial de compras
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/usuario/*")
                        .hasAnyRole("USER", "ADMIN")

                        // El vendedor solo ve la lista y el detalle (Anexo 1)
                        .requestMatchers(HttpMethod.GET, "/api/pedidos")
                        .hasAnyRole("VENDEDOR", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/pedidos/**")
                        .hasAnyRole("VENDEDOR", "ADMIN")

                        // El cliente confirma su compra
                        .requestMatchers(HttpMethod.POST, "/api/pedidos")
                        .hasAnyRole("USER", "ADMIN")

                        // Solo ADMIN puede actualizar estados
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado")
                        .hasRole("ADMIN")

                        // Solo ADMIN puede eliminar pedidos
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/*")
                        .hasRole("ADMIN")

                        .anyRequest().denyAll()

                        

                )
                .exceptionHandling(error -> error
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuracion = new CorsConfiguration();

        configuracion.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuracion.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        configuracion.setAllowedHeaders(
                List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuracion);

        return source;
    }
}
