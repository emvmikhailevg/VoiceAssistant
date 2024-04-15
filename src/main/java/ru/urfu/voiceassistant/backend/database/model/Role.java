package ru.urfu.voiceassistant.backend.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * Класс представляет роль пользователя в системе.
 */
@Entity
@Table(name="app_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * Идентификатор роли.
     */
    @Id
    @SequenceGenerator(name = "app_role_id_seq", sequenceName = "app_role_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_id_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Логин. Должен быть уникальным и не может быть пустым.
     */
    @Column(nullable = false, unique = true)
    @NotBlank
    private String login;

    /**
     * Список пользователей, имеющих данную роль.
     */
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
