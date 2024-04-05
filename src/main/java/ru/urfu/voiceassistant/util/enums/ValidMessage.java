package ru.urfu.voiceassistant.util.enums;

import lombok.Getter;

/**
 * Enum для хранения текстовых сообщений о нарушении валидации
 */
@Getter
public enum ValidMessage {

    ACCOUNT_EXISTS("Аккаунт с данным email уже существует"),
    PASSWORDS_ARE_DIFFERENT("Пароли не совпадают");

    private final String message;

    ValidMessage(String message) {
        this.message = message;
    }
}
