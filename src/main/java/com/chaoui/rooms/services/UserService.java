package com.chaoui.rooms.services;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User registerNewUser(User user) {
        user.getCredentials().setPassword(
            bCryptPasswordEncoder.encode(user.getCredentials().getPassword())
        );
        User savedUser = userRepository.save(user);
        System.out.println("User registered successfully: " + savedUser);

        return savedUser;
    }

    public UUID deleteUser(User user) {
        UUID id = user.getId();
        userRepository.delete(user);
        System.out.println("User deleted successfully: " + id);
        return id;
    }

    public User finUserByUsername(String username) {
        return userRepository.findByCredentials_Username(username).orElseThrow();
    }
}
