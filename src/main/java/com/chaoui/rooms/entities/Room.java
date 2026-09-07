package com.chaoui.rooms.entities;

import com.chaoui.rooms.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"topics"})
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
    private Set<UserRole> allowedRoles = new HashSet<>();

    @OneToMany(mappedBy = "room",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @BatchSize(size = 5)
    @Builder.Default
    private List<Topic> topics = new ArrayList<>();

}

