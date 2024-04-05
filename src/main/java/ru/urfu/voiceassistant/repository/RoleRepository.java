package ru.urfu.voiceassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.entity.role.Role;

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
    Role findByLogin(String name);
}
