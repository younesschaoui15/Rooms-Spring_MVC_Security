package com.chaoui.rooms.controllers;

import com.chaoui.rooms.DTOs.RegisterUserReqDTO;
import com.chaoui.rooms.DTOs.RegisterUserResDTO;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/administration")
@PreAuthorize("hasAnyRole('APP_ADMIN', 'APP_SUPERADMIN')")
public class AdministrationController {

    private final UserService userService;

    public AdministrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-user")
    public ResponseEntity<RegisterUserResDTO> registerNewUser(@Valid @RequestBody RegisterUserReqDTO userReqDTO) {
        User savedUser = userService.registerNewUser(userReqDTO);

        RegisterUserResDTO userResDTO = RegisterUserResDTO.builder()
            .id(savedUser.getId())
            .username(savedUser.getCredentials().getUsername())
            .email(savedUser.getEmail())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .roles(savedUser.getRoles())
            .enabled(savedUser.isEnabled())
            .build();

        return ResponseEntity.ok(userResDTO);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam("id") UUID userId) {
        userService.deleteUserById(userId);

        return ResponseEntity.ok("User deleted successfully");
    }
}
