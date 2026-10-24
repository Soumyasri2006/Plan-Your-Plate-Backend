
package com.planyourplate.config;

import com.planyourplate.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                
.csrf(csrf -> csrf.disable())
.cors(cors -> cors.configurationSource(request -> {
    org.springframework.web.cors.CorsConfiguration config =
            new org.springframework.web.cors.CorsConfiguration();

    config.setAllowedOrigins(
            java.util.List.of("http://localhost:5173")
    );

    config.setAllowedMethods(
            java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );

    config.setAllowedHeaders(
            java.util.List.of("Authorization", "Content-Type")
    );

    return config;
}))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/ingredients",
                                "/api/recipes/**",
                                "/api/recipe-ingredients",
                                "/api/recommendations",
                                "/api/nutrition/**",
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
