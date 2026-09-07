package com.chaoui.rooms.services;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.repositories.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoomService roomService;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, RoomService roomService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roomService = roomService;
    }

    @Transactional
    public User registerNewUser(User user) {
        user.getCredentials().setPassword(
            bCryptPasswordEncoder.encode(user.getCredentials().getPassword())
        );

        return userRepository.save(user);
    }

    public UUID deleteUser(User user) {
        userRepository.delete(user);

        return user.getId();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByCredentials_Username(username);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms(@NotBlank String username) {
        return getUserByUsername(username)
            .map(user -> roomService.getRoomsWithRoles(user.getRoles()))
            .orElse(Collections.emptyList());
    }
}
