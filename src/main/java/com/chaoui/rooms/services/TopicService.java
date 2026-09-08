package com.chaoui.rooms.services;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.exceptions.ContentNotFoundException;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.repositories.RoomRepository;
import com.chaoui.rooms.repositories.TopicRepository;
import com.chaoui.rooms.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public Topic getTopicById(Long id) throws ContentNotFoundException {
        return topicRepository.findById(id)
            .orElseThrow(() -> new ContentNotFoundException("Topic Not Found"));
    }

    @Transactional
    public Topic createTopic(Topic topic, Long roomId, UUID userId) throws RoomAccessDeniedException, ContentNotFoundException {
        if (roomId == null)
            throw new IllegalArgumentException("Room must be provided");
        if (userId == null)
            throw new IllegalArgumentException("User must be provided");

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ContentNotFoundException("Room not found!"));

        boolean isUserNotAllowed = Collections.disjoint(user.getRoles(), room.getAllowedRoles());
        if (isUserNotAllowed)
            throw new RoomAccessDeniedException("User is not allowed to create a topic in this room");

        topic.setUser(user);
        topic.setRoom(room);

        //Add user to replies
        if (!topic.getReplies().isEmpty())
            topic.getReplies().forEach(reply -> {
                reply.setTopic(topic);
                reply.setUser(user);
            });

        return createTopic(topic);
    }

    private Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
