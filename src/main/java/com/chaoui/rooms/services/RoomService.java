package com.chaoui.rooms.services;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.enums.UserRole;
import com.chaoui.rooms.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getRoomsWithRoles(Set<UserRole> roles) {
        return roomRepository.findDistinctByAllowedRoles(roles)
            .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<Topic> getRoomTopics(Long roomId) {
        return getRoomById(roomId)
            .map(Room::getTopics)
            .orElse(Collections.emptyList());
    }
}
