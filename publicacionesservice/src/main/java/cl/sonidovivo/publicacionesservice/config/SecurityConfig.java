package cl.sonidovivo.publicacionesservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, ex) ->
                                response.setStatus(
                                        HttpStatus.FORBIDDEN.value())))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()

                        // CONSULTAS PUBLICAS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/publicaciones",
                                "/api/v1/publicaciones/**")
                        .permitAll()

                        // CREACION EXCLUSIVA DE ADMINISTRADORES
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/publicaciones")
                        .hasRole("ADMIN")

                        // ACTUALIZACION EXCLUSIVA DE ADMINISTRADORES
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/publicaciones/**")
                        .hasRole("ADMIN")

                        // ELIMINACION EXCLUSIVA DE ADMINISTRADORES
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/publicaciones/**")
                        .hasRole("ADMIN")

                        .anyRequest().denyAll())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/api/**", configuration);

        return source;
    }
}