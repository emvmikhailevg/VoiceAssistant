package ru.urfu.voiceassistant.backend.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.backend.database.model.User;

/**
 * Интерфейс репозитория для взаимодействия с таблицей пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его электронной почте.
     *
     * @param email электронная почта пользователя.
     * @return пользователь с указанной электронной почтой.
     */
    User findUserByEmail(String email);

    /**
     * Находит пользователя по токену сброса пароля.
     *
     * @param resetToken токен сброса пароля пользователя.
     * @return пользователь с указанным токеном сброса пароля.
     */
    User findUserByResetToken(String resetToken);
}
