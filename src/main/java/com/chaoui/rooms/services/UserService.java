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

        return userRepository.save(user);
    }

    public UUID deleteUser(User user) {
        userRepository.delete(user);

        return user.getId();
    }

    public User finUserByUsername(String username) {
        return userRepository.findByCredentials_Username(username).orElseThrow();
    }

    public User finUserById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }
}
