package com.chaoui.rooms.entities;

import com.chaoui.rooms.enums.ContentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Announcement extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ContentStatus status = ContentStatus.PUBLISHED;

    @Column(nullable = false)
    @NotNull
    @Builder.Default
    private LocalDateTime expirationDateTime =  LocalDateTime.now().plusDays(3);

    @ManyToMany(mappedBy = "announcements", fetch = FetchType.LAZY, cascade = {PERSIST, DETACH, MERGE, REFRESH})
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    List<Room> rooms = new ArrayList<>();
}



