package com.chaoui.rooms.configurations.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@Slf4j
public class ListenersConfig {

    /*
     * Authentication success event listener
     * */
    @EventListener(id = "AuthenticationSuccessEvent", classes = AuthenticationSuccessEvent.class)
    public void setAuthenticationSuccessEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication auth = authenticationSuccessEvent.getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        log.info("""
            # Authentication Success:
                User Details: %s
            """.formatted(userDetails));
    }

    /*
     * Authentication failure event listener
     * */
    @EventListener(id = "AuthenticationFailureEvent", classes = AbstractAuthenticationFailureEvent.class)
    public void setAuthenticationFailureEvent(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
        Authentication auth = authenticationFailureEvent.getAuthentication();
        var isAuthenticated = auth.isAuthenticated();
        var name = auth.getName();
        var credentials = auth.getCredentials();
        var user = auth.getPrincipal();
        var details = auth.getDetails();
        var authorities = auth.getAuthorities();

        log.warn("""
            # Authentication Failure:
                Is Authenticated: %s
                Name: %s
                Credentials: %s
                User: %s
                Details: %s
                Authorities: %s
            """.formatted(isAuthenticated, name, credentials, user, details, authorities));
    }
}
