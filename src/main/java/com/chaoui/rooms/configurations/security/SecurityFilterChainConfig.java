package com.chaoui.rooms.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@Slf4j(topic = "SecurityFilterChainConfig")
public class SecurityFilterChainConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    @Qualifier("authenticationFailureHandler") AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
            .csrf(conf -> conf
                //For testing REST API
                .ignoringRequestMatchers("/administration/new-user")
            )
            .authorizeHttpRequests(conf -> conf
                .requestMatchers(
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            ///Basic auth: send credentials in the HTTP Authorization header (username:password Base64 encoded)
            //.httpBasic(Customizer.withDefaults())
            ///Enables username/password authentication through a custom HTML login form or Spring's default one
            .formLogin(conf -> conf
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/", true)
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
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .build();
    }
}