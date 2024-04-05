package ru.urfu.voiceassistant.util.enums;

import lombok.Getter;

/**
 * Enum для хранения текстовых атрибутов, передаваемых в модель.
 */
@Getter
public enum ModelMessageAttribute {

    USER_NOT_ACTIVATED_MESSAGE("Пользователь не активирован"),
    INSTRUCTIONS_TO_RESET_PASSWORD_MESSAGE("Пожалуйста, проверьте свой email для " +
            "получения инструкции по смене пароля"),
    TOKEN_NOT_EXIST("Токен не существует"),
    USER_ACTIVATED_MESSAGE("Пользователь успешно активировался"),
    USER_DOES_NOT_EXIST_MESSAGE("Такого пользователя не существует");

    private final String message;

    ModelMessageAttribute(String message) {
        this.message = message;
    }
}
