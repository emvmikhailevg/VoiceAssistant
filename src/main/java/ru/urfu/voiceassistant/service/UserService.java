package ru.urfu.voiceassistant.service;

import ru.urfu.voiceassistant.dto.UserDTO;
import ru.urfu.voiceassistant.entity.UserEntity;

/**
 * Сервис, предоставляющий функционал операций, связанных с пользователями.
 * Этот сервис обрабатывает регистрацию пользователей, активацию, управление паролями и
 * другие связанные с пользователем функциональности.
 */
public interface UserService {

    /**
     * Регистрирует нового пользователя на основе переданных данных.
     * Создает новую сущность пользователя, устанавливает необходимые параметры,
     * отправляет электронное письмо с кодом активации и сохраняет пользователя в репозитории.
     *
     * @param userDTO ДТО пользователя с данными для регистрации.
     */
    void registerUser(UserDTO userDTO);

    /**
     * Активирует пользователя на основе предоставленного кода активации.
     *
     * @param activationCode Код активации пользователя.
     * @return true, если активация прошла успешно, false в противном случае.
     */
    boolean activateUser(String activationCode);

    /**
     * Изменяет пароль пользователя.
     * Проверяет, что новый пароль отличается от предыдущего,
     * затем кодирует новый пароль и сохраняет изменения в репозитории.
     *
     * @param currentUser Сущность пользователя, для которого изменяется пароль.
     * @param newPassword Новый пароль пользователя.
     * @throws IllegalArgumentException Если новый пароль совпадает с предыдущим.
     */
    void updateUserPassword(UserEntity currentUser, String newPassword);
}
