package ru.urfu.voiceassistant.entity.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.voiceassistant.entity.UserEntity;

import java.util.List;

/**
 * Класс представляет роль пользователя в системе.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    /**
     * Идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Логин. Должен быть уникальным и не может быть пустым.
     */
    @Column(nullable = false, unique = true)
    private String login;

    /**
     * Список пользователей, имеющих данную роль.
     */
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;
}
