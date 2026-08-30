package com.chaoui.rooms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @RequestMapping("/list")
    public String list() {
        return "rooms/list";
    }
}
