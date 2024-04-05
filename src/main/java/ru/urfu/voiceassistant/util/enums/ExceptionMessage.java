package ru.urfu.voiceassistant.util.enums;

import lombok.Getter;

/**
 * Enum для хранения текстовых предупреждений при возникновении ошибок
 */
@Getter
public enum ExceptionMessage {

    UNSUPPORTED_FILE_TYPE_MESSAGE("Неподдерживаемый тип файла. Доступен только .mp3 и .wav"),
    INVALID_MAIL_OR_PASSWORD_MESSAGE("Почта или пароль введены неверно"),
    NEW_PASSWORD_SHOULD_BE_DIFFERENT_FROM_OLD_PASSWORD("Новый пароль должен отличаться от старого пароля"),
    ERROR_SAVING_FILE_MESSAGE("Ошибка загрузки файла: "),
    ERROR_DELETING_FILE_WITH_CODE_MESSAGE("Ошибка удаления файла с кодом: "),
    FILE_NOT_FOUND_WITH_ID_MESSAGE("Файл с данным id не найден: "),
    PERMISSION_ERROR_MESSAGE("У вас нет прав для удаления этого файла");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
