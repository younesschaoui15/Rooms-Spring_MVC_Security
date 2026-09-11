package com.chaoui.rooms.mappers;

import com.chaoui.rooms.DTOs.RegisterUserReqDTO;
import com.chaoui.rooms.DTOs.RegisterUserResDTO;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.entities.UserCredentials;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User toEntity(RegisterUserReqDTO userReqDTO) {
        return User.builder()
            .firstName(userReqDTO.firstName())
            .lastName(userReqDTO.lastName())
            .email(userReqDTO.email())
            .enabled(userReqDTO.enabled())
            .roles(userReqDTO.roles())
            .credentials(
                UserCredentials.builder()
                    .username(userReqDTO.username())
                    .password(bCryptPasswordEncoder.encode(userReqDTO.password()))
                    .build()
            )
            .build();
    }

    public <T> T toDTO(User user, @NotNull Class<T> clazz) {
        if (clazz == RegisterUserResDTO.class) {
            return clazz.cast(RegisterUserResDTO.builder()
                .id(user.getId())
                .username(user.getCredentials().getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .build());
        } else if (clazz == RegisterUserReqDTO.class) {
            return clazz.cast(RegisterUserReqDTO.builder()
                .username(user.getCredentials().getUsername())
                .password(user.getCredentials().getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .build());
        } else {
            return null;
        }
    }
}
