package com.chaoui.rooms.DTOs;

import com.chaoui.rooms.enums.UserRole;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record RegisterUserResDTO(
    UUID id,
    String username,
    String firstName,
    String lastName,
    String email,
    Set<UserRole> roles,
    boolean enabled
) {
}
