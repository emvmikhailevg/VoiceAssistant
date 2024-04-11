package ru.urfu.voiceassistant.backend.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.backend.database.model.Role;

/**
 * Интерфейс репозитория для взаимодействия с таблицей ролей в базе данных.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по её логину.
     *
     * @param name логин пользователя.
     * @return роль пользователя.
     */
    Role findRoleByLogin(String name);
}
