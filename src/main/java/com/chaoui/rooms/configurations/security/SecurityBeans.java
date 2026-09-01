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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SecurityBeans {

    @Bean
    AuthenticationProvider authenticationProvider(
        @Qualifier("JdbcUserDetailsManager") UserDetailsService userDetailsService,
        @Qualifier("bCryptPasswordEncoder") BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return daoAuthenticationProvider;
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

    /*
    * Password encoder
    * */
    @Bean(name = "bCryptPasswordEncoder")
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
