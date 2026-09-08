package com.chaoui.rooms.services;

import com.chaoui.rooms.configurations.security.AuthUser;
import com.chaoui.rooms.entities.Announcement;
import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.repositories.AnnouncementRepository;
import com.chaoui.rooms.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AnnouncementRepository announcementRepository;

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getRoomsWithRoles(Set<UserRole> roles) {
        return roomRepository.findRoomsByAllowedRoles(roles)
            .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<Topic> getRoomTopics(Long roomId) {
        return getRoomById(roomId)
            .map(room -> {
                List<Topic> topics = room.getTopics();
                // Initialize associations while the persistence context is open
                topics.forEach(topic -> {
                    if (topic.getUser() != null) {
                        topic.getUser().getCredentials().getUsername();
                    }
                    topic.getReplies().forEach(reply -> {
                        if (reply.getUser() != null) {
                            reply.getUser().getCredentials().getUsername();
                        }
                    });
                });

                return topics;
            })
            .orElse(Collections.emptyList());
    }

    public boolean isUserAccessAllowed(Room room, AuthUser authUser) {
        var roomAllowedRoles = room.getAllowedRoles().stream()
            .map(r -> "ROLE_" + r.name().toUpperCase())
            .toList();
        var userRoles = authUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return !Collections.disjoint(roomAllowedRoles, userRoles);
    }

    public List<Announcement> getFutureAnnouncements(Room room) {
        var now = LocalDateTime.now();

        return announcementRepository.findByRoomsAndExpirationDateTimeAfterOrderByExpirationDateTimeAsc(room, now);
    }
}
