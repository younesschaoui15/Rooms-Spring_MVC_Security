package com.chaoui.rooms.controllers;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administration")
@PreAuthorize("hasAnyRole('APP_ADMIN', 'APP_SUPERADMIN')")
public class AdministrationController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdministrationController(UserRepository userRepository,
                                    BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/new-user")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        System.out.println("User created successfully: "+ savedUser);
        return ResponseEntity.ok("User created successfully");
    }
}
