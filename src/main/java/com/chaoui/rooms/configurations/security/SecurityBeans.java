package com.chaoui.rooms.configurations.security;

import com.chaoui.rooms.enums.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityBeans {

    @Bean
    AuthenticationProvider authenticationProvider(@Qualifier("inMemoryUserDetailsManager") UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(...);
        return daoAuthenticationProvider;
    }

    @Bean(name = "inMemoryUserDetailsManager")
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin")
            .roles(UserRole.APP_ADMIN.name())
            .build();
        UserDetails developer = User.withUsername("dev")
            .password("{noop}dev")
            .roles(UserRole.DEVELOPER.name(), UserRole.TEAM_LEAD.name())
            .build();

        return new InMemoryUserDetailsManager(admin, developer);
    }

    @Bean(name = "userDetailsService")
    UserDetailsService userDetailsService() {
        return (String username) -> {
            UserDetails user = User.builder()
                .username(username)
                .password("{noop}user") //Hardcoded password
                .roles(UserRole.ROOM_MODERATOR.name())
                .build();

            return user;
        };
    }
}
