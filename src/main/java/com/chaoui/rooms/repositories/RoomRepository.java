package com.chaoui.rooms.repositories;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<List<Room>> findDistinctByAllowedRoles(Set<UserRole> allowedRoles);
}
