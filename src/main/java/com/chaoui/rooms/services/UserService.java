package com.chaoui.rooms.services;

import com.chaoui.rooms.DTOs.RegisterUserReqDTO;
import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.entities.UserCredentials;
import com.chaoui.rooms.exceptions.UserExistsException;
import com.chaoui.rooms.repositories.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoomService roomService;

    @Transactional
    public User registerNewUser(RegisterUserReqDTO userReqDTO) {
        getUserByUsername(userReqDTO.username())
            .ifPresentOrElse(u -> {
                throw new UserExistsException(userReqDTO.username());
            }, () -> userRepository.findByEmail(userReqDTO.email())
                .ifPresent(u -> {
                    throw new UserExistsException(userReqDTO.email());
                }));

        User user = User.builder()
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

        user.getCredentials().setUser(user);

        return userRepository.save(user);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByCredentials_Username(username);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms(@NotBlank UUID userId) {
        return getUserById(userId)
            .map(user -> roomService.getRoomsWithRoles(user.getRoles()))
            .orElse(Collections.emptyList());
    }
}
