package com.chaoui.rooms.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(conf -> conf
                .requestMatchers(
                    "/login/**",
                    "/logout/**",
                    "/error/**"
                ).permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}