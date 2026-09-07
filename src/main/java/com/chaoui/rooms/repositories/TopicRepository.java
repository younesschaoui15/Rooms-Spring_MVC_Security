package com.chaoui.rooms.repositories;

import com.chaoui.rooms.entities.Topic;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @EntityGraph(attributePaths = "replies")
    Optional<Topic> findById(@NonNull Long id);
    Page<Topic> findAll(@NonNull Pageable pageable);
}
