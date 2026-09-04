package com.chaoui.rooms.utils;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.entities.UserCredentials;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.services.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class InitDatabaseSchema {

    private final UserService userService;

    public InitDatabaseSchema(UserService userService) {
        this.userService = userService;
    }

    public void initUsers() {
        List<User> users = List.of(
            new User(null, "Youness", "CHAOUI", "admin1@mail.com", true, Set.of(UserRole.APP_ADMIN, UserRole.APP_SUPER_ADMIN),
                new UserCredentials(null, "admin1", "admin1", null)),
            new User(null, "Ahmad", "AHMADI", "dev1@mail.com", true, Set.of(UserRole.DEVELOPER),
                new UserCredentials(null, "dev1", "dev1", null)),
            new User(null, "Samira", "SAMIRI", "manager1@mail.com", true, Set.of(UserRole.PROJECT_MANAGER),
                new UserCredentials(null, "manager1", "manager1", null))
        );

        users.forEach(user -> {
            try {
                userService.registerNewUser(user);
                Thread.sleep(200);
            } catch (Exception e) {
                System.err.println("# Error: " + e.getMessage());
            }
        });
    }

    private Optional<UUID> deleteUserByUsername(String username) {
        try {
            return Optional.of(userService.deleteUser(userService.finUserByUsername(username)));
        } catch (Exception e) {
            System.err.println("# Error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
