package ru.urfu.voiceassistant.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс DTO (Data Transfer Object) для представления данных о пользователе.
 * Содержит информацию о логине, электронной почте, имени, фамилии, дне рождения,
 * номере телефона, пароле и подтверждении пароля.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * Логин пользователя. Не может быть пустым.
     */
    @NotEmpty(message = "Логин не может быть пустым")
    private String login;

    /**
     * Электронная почта пользователя. Не может быть пустой и должна быть валидной.
     */
    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным")
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
     * Номер телефона пользователя
     */
    private String number;

    /**
     * Пароль пользователя. Не может быть пустым и должен соответствовать заданным критериям:
     * - Длина от 8 до 16 символов
     * - Содержит хотя бы одну заглавную букву
     * - Содержит хотя бы одну цифру
     */
    @NotEmpty(message = "Пароль не может быть пустым")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$",
            message = "Ваш пароль должен состоять от 8 до 16 символов, " +
                    "включая хотя бы одну заглавную букву и одну цифру")
    private String password;

    /**
     * Подтверждение пароля пользователя. Не может быть пустым.
     */
    @NotEmpty(message = "Подтверждаемый пароль не может быть пустым")
    private String confirmPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
