package com.chaoui.rooms.utils;

import com.chaoui.rooms.entities.*;
import com.chaoui.rooms.enums.Importance;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.services.RoomService;
import com.chaoui.rooms.services.TopicService;
import com.chaoui.rooms.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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
                .announcements(List.of(
                    Announcement.builder()
                        .importance(Importance.CRITICAL)
                        .expirationDateTime(LocalDateTime.now().plusDays(1))
                        .content("Production deploy freeze starts tonight — merge only hotfix branches.")
                        .build(),
                    Announcement.builder()
                        .importance(Importance.HIGH)
                        .expirationDateTime(LocalDateTime.now().plusDays(3))
                        .content("Security patch review meeting on Friday. Bring open CVEs.")
                        .build(),
                    Announcement.builder()
                        .importance(Importance.MEDIUM)
                        .expirationDateTime(LocalDateTime.now().plusDays(7))
                        .content("Update your local JDK to 21 before next Monday.")
                        .build(),
                    Announcement.builder()
                        .importance(Importance.LOW)
                        .expirationDateTime(LocalDateTime.now().plusDays(14))
                        .content("New sticky notes arrived in the kitchen. Grab some for your desk.")
                        .build()
                ))
                .build(),
            Room.builder()
                .name("Managers Room")
                .description("Room only for managers")
                .allowedRoles(Set.of(UserRole.PROJECT_MANAGER))
                .announcements(List.of(
                    Announcement.builder()
                        .importance(Importance.HIGH)
                        .expirationDateTime(LocalDateTime.now().plusDays(2))
                        .content("Sprint planning slides due before standup tomorrow.")
                        .build(),
                    Announcement.builder()
                        .importance(Importance.MEDIUM)
                        .expirationDateTime(LocalDateTime.now().plusDays(5))
                        .content("Quarterly budget estimates need your sign-off.")
                        .build()
                ))
                .build(),
            Room.builder()
                .name("Managers & POs Room")
                .description("Room for managers and product owners")
                .allowedRoles(Set.of(UserRole.PRODUCT_OWNER, UserRole.PROJECT_MANAGER))
                .announcements(List.of(
                    Announcement.builder()
                        .importance(Importance.MEDIUM)
                        .expirationDateTime(LocalDateTime.now().plusDays(10))
                        .content("Don't forget to finish your tasks for this sprint!")
                        .build()
                ))
                .build()
        );

        return rooms.stream()
            .map(roomService::createRoom)
            .toList();
    }

    @Transactional
    public List<Topic> initTopics(UUID userId, Long roomId) {
        String text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem";

        List<Topic> topics = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> Topic.builder()
                .title("Topic " + i + " - Room: " + roomId)
                .post("Topic " + i + " post " + text)
                .replies(List.of(
                    new Reply(null, "comment " + i, null, null, null),
                    new Reply(null, "second comment " + i, null, null, null)
                ))
                .build())
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

        return topics;
    }

    @Transactional
    public Optional<UUID> deleteUserById(UUID id) {
        try {
            User user = userService.getUserById(id);
            return Optional.of(userService.deleteUser(user));
        } catch (Exception e) {
            System.err.println("# Error (deleteUserById) : " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    protected Optional<UUID> deleteUserByUsername(String username) {
        try {
            User user = userService.getUserByUsername(username).orElseThrow();
            return Optional.of(userService.deleteUser(user));
        } catch (Exception e) {
            System.err.println("# Error (deleteUserByUsername) : " + e.getMessage());
            return Optional.empty();
        }
    }
}
