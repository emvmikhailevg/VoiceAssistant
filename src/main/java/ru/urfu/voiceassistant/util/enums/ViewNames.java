package ru.urfu.voiceassistant.util.enums;

import lombok.Getter;

/**
 * Enum для хранения имен представлений.
 */
@Getter
public enum ViewNames {

    INDEX_PAGE("index"),
    LOGIN_PAGE("login"),
    PASSWORD_RESET_PAGE("passwordResetPage"),
    PERSONAL_PAGE("personalPage"),
    RECORD_FILE_PAGE("recordFilePage"),
    RECOVERY_PASSWORD_PAGE("recoveryPasswordPage"),
    REGISTER_PAGE("register"),
    UPLOAD_FILE_PAGE("uploadFile");

    private final String name;

    ViewNames(String name) {
        this.name = name;
    }
}
