package com.chaoui.rooms.controllers;

import com.chaoui.rooms.services.RoomService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/list")
    public String list() {
        return "rooms/list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        var room = roomService.getRoomById(id);
        var topics = roomService.getRoomTopics(id);

        model.addAttribute("room", room.orElse(null));
        model.addAttribute("topics", topics);

        return "rooms/detail";
    }
}
