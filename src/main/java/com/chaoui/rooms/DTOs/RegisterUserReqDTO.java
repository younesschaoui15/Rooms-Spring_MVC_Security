package com.chaoui.rooms.DTOs;

import com.chaoui.rooms.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record RegisterUserReqDTO(
    @NotBlank String username,
    @NotBlank @Size(min = 4) String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    @NotNull @Size(min = 1) Set<UserRole> roles,
    boolean enabled
) {
}
