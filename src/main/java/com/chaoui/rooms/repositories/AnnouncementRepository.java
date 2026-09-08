package com.chaoui.rooms.repositories;

import com.chaoui.rooms.entities.Announcement;
import com.chaoui.rooms.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByRoomsAndExpirationDateTimeAfterOrderByExpirationDateTimeAsc(Room room, LocalDateTime expirationDateTime);
}
