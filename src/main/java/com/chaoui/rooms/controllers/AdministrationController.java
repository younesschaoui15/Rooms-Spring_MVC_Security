package com.chaoui.rooms.controllers;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administration")
@PreAuthorize("hasAnyRole('APP_ADMIN', 'APP_SUPERADMIN')")
public class AdministrationController {

    private final UserService userService;

    public AdministrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-user")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody User user) {
        User savedUser = userService.registerNewUser(user);
        return ResponseEntity.ok("User created successfully with username: " + savedUser.getCredentials().getUsername());
    }
}
