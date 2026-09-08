package com.chaoui.rooms.controllers;

import com.chaoui.rooms.configurations.security.AuthUser;
import com.chaoui.rooms.entities.Reply;
import com.chaoui.rooms.exceptions.ContentNotFoundException;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.services.ReplyService;
import com.chaoui.rooms.services.RoomService;
import com.chaoui.rooms.services.TopicService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final RoomService roomService;
    private final TopicService topicService;
    private final ReplyService replyService;

    @PostMapping("/{roomId}/{topicId}/replies")
    public String addReply(@PathVariable Long roomId,
                           @PathVariable Long topicId,
                           @NotBlank @RequestParam(required = false) String content,
                           @AuthenticationPrincipal AuthUser authUser,
                           RedirectAttributes redirectAttributes)
        throws ContentNotFoundException {

        var topic = topicService.getTopicById(topicId);
        var room = topic.getRoom();

        if (!roomService.isUserAccessAllowed(room, authUser))
            throw new ContentNotFoundException("User is not allowed to access this room");

        if (content == null || content.isBlank()) {
            redirectAttributes.addFlashAttribute("replyError", "Reply cannot be empty.");
            return "redirect:/rooms/" + roomId + "/topics#topic-" + topicId;
        }

        try {
            Reply reply = replyService.createReply(roomId, topicId, authUser, content);
            redirectAttributes.addFlashAttribute("replySuccess", "Reply posted.");
        } catch (RoomAccessDeniedException e) {
            throw new ContentNotFoundException("User is not allowed to reply in this room");
        }

        return "redirect:/rooms/" + roomId + "/topics#topic-" + topicId;
    }
}
