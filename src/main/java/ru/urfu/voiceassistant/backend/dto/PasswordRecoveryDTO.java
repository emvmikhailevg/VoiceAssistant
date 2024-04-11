package ru.urfu.voiceassistant.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Класс DTO (Data Transfer Object) для представления данных о восстановлении пароля.
 * Содержит информацию о новом пароле и его подтверждении.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryDTO {

    /**
     * Новый пароль.
     * Должен быть непустым и соответствовать заданным критериям:
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
     * Подтверждение нового пароля. Не может быть пустым.
     */
    @NotEmpty(message = "Подтверждаемый пароль не может быть пустым")
    private String confirmPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordRecoveryDTO that = (PasswordRecoveryDTO) o;
        return Objects.equals(confirmPassword, that.confirmPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmPassword);
    }
}
