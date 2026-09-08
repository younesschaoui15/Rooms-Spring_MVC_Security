package com.chaoui.rooms.services;

import com.chaoui.rooms.configurations.security.AuthUser;
import com.chaoui.rooms.entities.Reply;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.enums.ContentStatus;
import com.chaoui.rooms.exceptions.ContentNotFoundException;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.repositories.ReplyRepository;
import com.chaoui.rooms.repositories.TopicRepository;
import com.chaoui.rooms.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;

    @Transactional
    public Reply createReply(Long roomId, Long topicId, AuthUser authUser, String content)
        throws ContentNotFoundException, RoomAccessDeniedException {

        if (content == null || content.isBlank())
            throw new IllegalArgumentException("Reply content must not be blank");

        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new ContentNotFoundException("Topic Not Found"));

        if (topic.getRoom() == null || !topic.getRoom().getId().equals(roomId))
            throw new ContentNotFoundException("Topic Not Found in this room");

        User user = userRepository.findById(authUser.getId())
            .orElseThrow(() -> new ContentNotFoundException("User Not Found"));

        boolean isUserNotAllowed = !roomService.isUserAccessAllowed(topic.getRoom(), authUser);
        if (isUserNotAllowed)
            throw new RoomAccessDeniedException("User is not allowed to reply in this room");

        Reply reply = Reply.builder()
            .content(content.trim())
            .status(ContentStatus.PUBLISHED)
            .topic(topic)
            .user(user)
            .build();

        topic.getReplies().add(reply);

        return replyRepository.save(reply);
    }
}
