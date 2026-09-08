package com.chaoui.rooms.controllers;

import com.chaoui.rooms.configurations.security.AuthUser;
import com.chaoui.rooms.enums.ContentStatus;
import com.chaoui.rooms.exceptions.ContentNotFoundException;
import com.chaoui.rooms.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}/topics")
    public String topics(@PathVariable Long id,
                         @AuthenticationPrincipal AuthUser authUser,
                         Model model) throws ContentNotFoundException {
        var room = roomService.getRoomById(id)
            .orElseThrow(() -> new ContentNotFoundException("Room Not Found"));

        if (!roomService.isUserAccessAllowed(room, authUser))
            throw new ContentNotFoundException("User is not allowed to access this room");

        var topics = room.getTopics();
        var announcements = roomService.getFutureAnnouncements(room).stream()
            .filter(a -> a.getStatus() == ContentStatus.PUBLISHED) //Only published announcements
            .toList();

        model.addAttribute("room", room);
        model.addAttribute("topics", topics);
        model.addAttribute("announcements", announcements);

        return "rooms/topics";
    }
}
