package com.chaoui.rooms.entities;

import com.chaoui.rooms.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;


@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Room extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(length = 512)
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<UserRole> allowedRoles = new HashSet<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 5)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Topic> topics = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {PERSIST, DETACH, MERGE, REFRESH})
    @JoinTable(
        name = "rooms_announcements",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "announcement_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    List<Announcement> announcements = new ArrayList<>();

}

