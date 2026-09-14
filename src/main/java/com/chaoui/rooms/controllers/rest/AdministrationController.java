package com.chaoui.rooms.controllers.rest;

import com.chaoui.rooms.DTOs.RegisterUserReqDTO;
import com.chaoui.rooms.DTOs.RegisterUserResDTO;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.mappers.UserMapper;
import com.chaoui.rooms.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/{version}/administration", version = "1")
@PreAuthorize("hasAnyRole('APP_ADMIN', 'APP_SUPERADMIN')")
@RequiredArgsConstructor
public class AdministrationController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/new-user")
    public ResponseEntity<RegisterUserResDTO> registerNewUser(@Valid @RequestBody RegisterUserReqDTO userReqDTO) {
        User savedUser = userService.registerNewUser(userReqDTO);
        RegisterUserResDTO userResDTO = userMapper.toDTO(savedUser, RegisterUserResDTO.class);

        return ResponseEntity.ok(userResDTO);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam("id") UUID userId) {
        userService.deleteUserById(userId);

        return ResponseEntity.ok("User with id='"+userId+"' deleted successfully");
    }
}
