package ru.urfu.voiceassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.entity.UserEntity;

/**
 * Интерфейс репозитория для взаимодействия с таблицей пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Находит пользователя по его электронной почте.
     *
     * @param email электронная почта пользователя.
     * @return пользователь с указанной электронной почтой.
     */
    UserEntity findByEmail(String email);

    /**
     * Находит пользователя по коду активации.
     *
     * @param activationCode код активации пользователя.
     * @return пользователь с указанным кодом активации.
     */
    UserEntity findUserEntityByActivationCode(String activationCode);

    /**
     * Находит пользователя по токену сброса пароля.
     *
     * @param resetToken токен сброса пароля пользователя.
     * @return пользователь с указанным токеном сброса пароля.
     */
    UserEntity findUserEntityByResetToken(String resetToken);
}
