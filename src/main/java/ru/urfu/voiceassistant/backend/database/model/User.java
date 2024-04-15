package ru.urfu.voiceassistant.backend.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс представляет сущность пользователя в системе.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @SequenceGenerator(name = "app_user_id_seq", sequenceName = "app_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_id_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Логин пользователя. Обязателен для заполнения.
     */
    @Column(nullable = false)
    @NotBlank
    private String login;

    /**
     * Электронная почта пользователя. Обязательна для заполнения и должна быть уникальной.
     */
    @Column(nullable = false, unique = true)
    @NotBlank
    private String email;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Фамилия пользователя.
     */
    private String surname;

    /**
     * Дата рождения пользователя.
     */
    private String birthday;

    /**
     * Номер телефона пользователя. Должен быть уникальным.
     */
    @Column(unique = true)
    private String number;

    /**
     * Пароль пользователя. Обязателен для заполнения.
     */
    @Column(nullable = false)
    @NotBlank
    private String password;

    /**
     * Дата и время создания записи о пользователе.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Токен сброса пароля.
     */
    private String resetToken;

    /**
     * Список ролей пользователя. FetchType.EAGER используется для немедленной загрузки ролей.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="app_users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

    /**
     * Список файлов, принадлежащих пользователю.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> files = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
