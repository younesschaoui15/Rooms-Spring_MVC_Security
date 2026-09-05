package com.chaoui.rooms.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "user_credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @OneToOne(mappedBy = "credentials")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}