package com.chaoui.rooms.configurations.security;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class SecurityBeansConfig {

    @Bean
    AuthenticationProvider authenticationProvider(
        @Qualifier("userDetailsService") UserDetailsService userDetailsService,
        @Qualifier("bCryptPasswordEncoder") BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return daoAuthenticationProvider;
    }

    @Bean(name = "userDetailsService")
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return (String username) -> {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));

            String[] userRoles = user.getRoles().stream()
                .map(Enum::name)
                .toArray(String[]::new);

            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(userRoles)
                .build();
        };
    }

    /*
     * JDBC users from database table "users"
     * */
    @Bean(name = "JdbcUserDetailsManager")
    UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcManager = new JdbcUserDetailsManager(dataSource);

        //Map authorities list from user's role list "roles" columns in the database "users" table
        jdbcManager.setAuthoritiesByUsernameQuery("""
            select username, unnest(string_to_array(roles, ',')) as authority
            from "users" where username = ?
            """);

        return jdbcManager;
    }

    /*
     * In memory users
     * */
    @Bean(name = "inMemoryUserDetailsManager")
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails admin = org.springframework.security.core.userdetails.User.withUsername("admin")
            .password("{noop}admin")
            .roles(UserRole.APP_ADMIN.name())
            .build();
        UserDetails developer = org.springframework.security.core.userdetails.User.withUsername("dev")
            .password("{noop}dev")
            .roles(UserRole.DEVELOPER.name(), UserRole.TEAM_LEAD.name())
            .build();

        return new InMemoryUserDetailsManager(admin, developer);
    }

    /*
     * Password encoder
     * */
    @Bean(name = "bCryptPasswordEncoder")
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Authentication failure handler (ex: try to access /endpoint without authentication)
     * */
    @Bean("authenticationFailureHandler")
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("Authentication is required for request [{}] : {}", request.getRequestURL(), authException.getMessage());
            response.sendRedirect(request.getContextPath() + "/login?error="+authException.getMessage());
        };
    }

    /*
    * Login failure handler (ex: logging in with invalid username or password)
    * */
    @Bean("loginFailureHandler")
    AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, authException) -> {
            log.warn("Login failure : {}", authException.getMessage());
            response.sendRedirect(request.getContextPath() + "/login?error=Invalid username or password");
        };
    }
}
