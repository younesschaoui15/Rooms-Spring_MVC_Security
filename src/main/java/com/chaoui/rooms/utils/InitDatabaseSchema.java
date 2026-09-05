package com.chaoui.rooms.utils;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.entities.UserCredentials;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.services.RoomService;
import com.chaoui.rooms.services.TopicService;
import com.chaoui.rooms.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class InitDatabaseSchema {

    private final UserService userService;
    private final TopicService topicService;
    private final RoomService roomService;

    public InitDatabaseSchema(UserService userService,
                              TopicService topicService,
                              RoomService roomService) {
        this.userService = userService;
        this.topicService = topicService;
        this.roomService = roomService;
    }

    @Transactional
    public List<User> initUsers() {
        List<User> users = List.of(
            User.builder().firstName("Youness").lastName("CHAOUI").email("admin1@mail.com").enabled(true)
                .roles(Set.of(UserRole.APP_ADMIN, UserRole.APP_SUPER_ADMIN))
                .credentials(new UserCredentials(null, "admin1", "admin1", null)).build(),
            User.builder().firstName("Ahmad").lastName("AHMADI").email("dev1@mail.com").enabled(true)
                .roles(Set.of(UserRole.DEVELOPER, UserRole.TEAM_LEAD))
                .credentials(new UserCredentials(null, "dev1", "dev1", null)).build(),
            User.builder().firstName("Siham").lastName("SIHAMI").email("dev2@mail.com").enabled(true)
                .roles(Set.of(UserRole.DEVELOPER))
                .credentials(new UserCredentials(null, "dev2", "dev2", null)).build(),
            User.builder().firstName("Samira").lastName("SAMIRI").email("manager1@mail.com").enabled(true)
                .roles(Set.of(UserRole.PROJECT_MANAGER))
                .credentials(new UserCredentials(null, "manager1", "manager1", null)).build(),
            User.builder().firstName("Halima").lastName("HALIMI").email("po1@mail.com").enabled(true)
                .roles(Set.of(UserRole.PRODUCT_OWNER))
                .credentials(new UserCredentials(null, "po1", "po1", null)).build()
        );

        return users.stream()
            .map(userService::registerNewUser)
            .toList();
    }

    @Transactional
    public List<Room> initRooms() {
        List<Room> rooms = List.of(
            Room.builder()
                .name("IT Room")
                .description("Room only for geeks")
                .allowedRoles(Set.of(UserRole.DEVELOPER, UserRole.TEAM_LEAD))
                .build(),
            Room.builder()
                .name("Managers Room")
                .description("Room only for managers")
                .allowedRoles(Set.of(UserRole.PRODUCT_OWNER, UserRole.PROJECT_MANAGER))
                .build()
        );

        return rooms.stream()
            .map(roomService::createRoom)
            .toList();
    }

    @Transactional
    public List<Topic> initTopics(UUID userId, Long roomId) {

        List<Topic> topics = List.of(
            Topic.builder()
                .title("Topic 1")
                .post("Topic 1 post...")
                .build(),
            Topic.builder()
                .title("Topic 2")
                .post("Topic 2 post...")
                .build()
        );

        return topics.stream()
            .map(topic -> {
                try {
                    return topicService.createTopic(topic, roomId, userId);
                } catch (RoomAccessDeniedException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();
    }

    @Transactional
    public Optional<UUID> deleteUserById(UUID id) {
        try {
            User user = userService.finUserById(id);
            return Optional.of(userService.deleteUser(user));
        } catch (Exception e) {
            System.err.println("# Error (deleteUserById) : " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    protected Optional<UUID> deleteUserByUsername(String username) {
        try {
            User user = userService.finUserByUsername(username);
            return Optional.of(userService.deleteUser(user));
        } catch (Exception e) {
            System.err.println("# Error (deleteUserByUsername) : " + e.getMessage());
            return Optional.empty();
        }
    }
}
