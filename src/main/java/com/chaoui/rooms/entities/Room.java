package com.chaoui.rooms.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.UUID;


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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column(length = 512)
    private String description;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Topic> topics;

}

