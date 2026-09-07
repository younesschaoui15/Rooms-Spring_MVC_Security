package com.chaoui.rooms.services;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.Topic;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.exceptions.RoomAccessDeniedException;
import com.chaoui.rooms.repositories.RoomRepository;
import com.chaoui.rooms.repositories.TopicRepository;
import com.chaoui.rooms.repositories.UserRepository;
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

    @Transactional
    public Topic createTopic(Topic topic, Long roomId, UUID userId) throws RoomAccessDeniedException {
        if (roomId == null)
            throw new IllegalArgumentException("Room must be provided");
        if (userId == null)
            throw new IllegalArgumentException("User must be provided");

        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        boolean isUserNotAllowed = Collections.disjoint(user.getRoles(), room.getAllowedRoles());
        if (isUserNotAllowed)
            throw new RoomAccessDeniedException("User is not allowed to create a topic in this room");

        topic.setUser(user);
        topic.setRoom(room);

        //Add user to replies
        if (!topic.getReplies().isEmpty())
            topic.getReplies().forEach(reply -> reply.setUser(user));

        return createTopic(topic);
    }

    private Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
