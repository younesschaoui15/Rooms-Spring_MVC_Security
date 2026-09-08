package com.chaoui.rooms.configurations.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AuthUser implements UserDetails {

    private UUID id;
    private String username;
    private String password;
    @Builder.Default
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean accountLocked = false;
    @Builder.Default
    private boolean credentialsExpired = false;
    @Builder.Default
    private boolean disabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

//    public void setAuthorities(Set<UserRole> roles) {
//        this.
////        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
////        for (String role : roles) {
////            Assert.isTrue(!role.startsWith("ROLE_"),
////                () -> role + " cannot start with ROLE_ (it is automatically added)");
////            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
////        }
////        this.authorities = authorities;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return !disabled;
    }
}
