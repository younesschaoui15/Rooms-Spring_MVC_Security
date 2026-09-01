package com.chaoui.rooms.entities;

import com.chaoui.rooms.coverters.UserRoleSetConverter;
import com.chaoui.rooms.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    @Email
    private String email;
    @Column
    private boolean enabled;
    @Convert(converter = UserRoleSetConverter.class)
    @Column(name = "roles")
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();
}


