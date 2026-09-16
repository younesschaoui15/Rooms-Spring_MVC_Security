package com.chaoui.rooms.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityFilterChainConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    @Qualifier("loginFailureHandler") AuthenticationFailureHandler loginFailureHandler,
                                    @Qualifier("authenticationFailureHandler") AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
            .csrf(conf -> conf
                //For testing REST API
                .ignoringRequestMatchers(
                    "/api/v1/administration/new-user",
                    "/api/v1/administration/delete-user"
                )
            )
            .authorizeHttpRequests(conf -> conf
                .requestMatchers(
                    "/login/**",
                    "/error",
                    "/favicon.ico"
                ).permitAll()
                .anyRequest().authenticated()
            )
            ///Basic auth: send credentials in the HTTP Authorization header (username:password Base64 encoded)
            .httpBasic(Customizer.withDefaults())
            ///Enables username/password authentication through a custom HTML login form or Spring's default one
            .formLogin(conf -> conf
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/", true)
                .failureHandler(loginFailureHandler) //Login failure handler
                .permitAll()
            )
            .logout(conf -> conf
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(conf -> conf
                .accessDeniedPage("/access-denied")
                .authenticationEntryPoint(authenticationEntryPoint) //Authentication failure handler
            )
            .build();
    }
}