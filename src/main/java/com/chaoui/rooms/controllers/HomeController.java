package com.chaoui.rooms.controllers;

import com.chaoui.rooms.configurations.security.AuthUser;
import com.chaoui.rooms.services.RoomService;
import com.chaoui.rooms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final RoomService roomService;
    private final UserService userService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal AuthUser authenticatedUser,
                       SecurityContext context,
                       Model model) {
//        System.out.println("###### Home Controller ######");
//        System.out.println("# Context: " + context.toString());
//        System.out.println("# UserDetails: " + authenticatedUser);

        var rooms = userService.getRooms(authenticatedUser.getId());
        model.addAttribute("rooms", rooms);

        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('APP_ADMIN', 'APP_SUPER_ADMIN')")
    public String admin(@AuthenticationPrincipal UserDetails userDetails,
                        Model model) {
        model.addAttribute("user", userDetails);
        return "security/access-test";
    }

    @GetMapping("/dev")
    @PreAuthorize("hasRole('DEVELOPER')")
    public String developer() {
        return "security/access-test";
    }
}
