package com.chaoui.rooms.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails,
                       SecurityContext context) {
//        System.out.println("###### Home Controller ######");
//        System.out.println("# Context: " + context.toString());
//        System.out.println("# UserDetails: " + userDetails);

        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
