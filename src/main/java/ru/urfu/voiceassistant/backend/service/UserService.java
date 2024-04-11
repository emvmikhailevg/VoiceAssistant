package ru.urfu.voiceassistant.backend.service;

import ru.urfu.voiceassistant.backend.dto.UserDTO;
import ru.urfu.voiceassistant.backend.database.model.User;

/**
 * Сервис, предоставляющий функционал операций, связанных с пользователями.
 * Этот сервис обрабатывает поиск, регистрацию пользователей, данные для обновления пароля,
 * и сохранение.
 */
public interface UserService {

    User findUserByEmail(String user);

    /**
     * Регистрирует нового пользователя на основе переданных данных.
     * Создает новую сущность пользователя, устанавливает необходимые параметры,
     * отправляет электронное письмо с кодом активации и сохраняет пользователя в репозитории.
     *
     * @param userDTO ДТО пользователя с данными для регистрации.
     */
    void registerUser(UserDTO userDTO);

    /**
     * Изменяет пароль пользователя.
     * Проверяет, что новый пароль отличается от предыдущего,
     * затем кодирует новый пароль и сохраняет изменения в репозитории.
     *
     * @param currentUser Сущность пользователя, для которого изменяется пароль.
     * @param newPassword Новый пароль пользователя.
     * @throws IllegalArgumentException Если новый пароль совпадает с предыдущим.
     */
    void updateUserPassword(User currentUser, String newPassword);

    /**
     * Сохраняет пользователя.
     *
     * @param currentUser Сущность пользователя, которого нужно сохрнаить.
     */
    void saveUser(User currentUser);

    /**
     * Ищет пользователя по токену смены пароля.
     *
     * @param token токен смены пароля.
     */
    User findUserByResetToken(String token);
}
