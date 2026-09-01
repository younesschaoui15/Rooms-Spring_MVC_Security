package com.chaoui.rooms.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(conf -> conf
                .requestMatchers(
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            ///Basic auth: send credentials in the HTTP Authorization header (username:password Base64 encoded)
            .httpBasic(Customizer.withDefaults())
            ///Enables username/password authentication through a custom HTML login form or Spring's default one
            .formLogin(conf -> conf
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .permitAll()
            )
            .logout(conf -> conf
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(conf -> conf
                .accessDeniedPage("/access-denied")
            )
            .build();
    }
}