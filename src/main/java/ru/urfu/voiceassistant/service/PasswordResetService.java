package ru.urfu.voiceassistant.service;

import ru.urfu.voiceassistant.entity.UserEntity;

/**
 * Сервис, предоставляющий функционал для сброса пароля.
 * Этот сервис обеспечивает генерацию токена сброса пароля и отправку инструкций для изменения пароля
 * по электронной почте.
 */
public interface PasswordResetService {

    /**
     * Генерирует уникальный токен для сброса пароля пользователя.
     *
     * @param user Сущность пользователя, для которого генерируется токен сброса пароля.
     * @return Уникальный токен сброса пароля.
     */
    String generateResetToken(UserEntity user);

    /**
     * Отправляет инструкции для смены пароля пользователю по электронной почте.
     *
     * @param user Сущность пользователя, для которого отправляются инструкции по сбросу пароля.
     */
    void sendInstructionsToChangePassword(UserEntity user);
}
