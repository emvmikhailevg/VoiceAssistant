package ru.urfu.voiceassistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.urfu.voiceassistant.entity.role.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет сущность пользователя в системе.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Логин пользователя. Обязателен для заполнения.
     */
    @Column(nullable = false)
    private String login;

    /**
     * Электронная почта пользователя. Обязательна для заполнения и должна быть уникальной.
     */
    @Column(nullable = false, unique = true)
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
    private String password;

    /**
     * Дата и время создания записи о пользователе.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Код активации для пользователя.
     */
    private String activationCode;

    /**
     * Токен сброса пароля.
     */
    private String resetToken;

    /**
     * Список ролей пользователя. FetchType.EAGER используется для немедленной загрузки ролей.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

    /**
     * Список файлов, принадлежащих пользователю.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileEntity> files = new ArrayList<>();

    /**
     * Переопределение метода toString() для удобства отображения информации о пользователе.
     *
     * @return строка, представляющая объект {@link UserEntity}.
     */
    @Override
    public String toString() {
        return "login = " + login + ", email = " + email;
    }
}
