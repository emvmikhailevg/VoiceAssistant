package ru.urfu.voiceassistant.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.voiceassistant.backend.dto.UserDTO;
import ru.urfu.voiceassistant.backend.database.model.User;
import ru.urfu.voiceassistant.backend.database.model.Role;
import ru.urfu.voiceassistant.backend.database.repository.RoleRepository;
import ru.urfu.voiceassistant.backend.database.repository.UserRepository;
import ru.urfu.voiceassistant.backend.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация интерфейса {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструирует новый объект {@link UserServiceImpl} с указанными зависимостями.
     *
     * @param userRepository       Репозиторий пользователей.
     * @param roleRepository       Репозиторий ролей.
     * @param passwordEncoder      Кодировщик паролей.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = createUserFromDTO(userDTO);
        Role role = getOrCreateUserRole();

        user.setRoles(List.of(role));

        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(User currentUser, String newPassword) {
        validateNewPassword(currentUser, newPassword);

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        currentUser.setResetToken(null);

        userRepository.save(currentUser);
    }

    @Override
    public void saveUser(User currentUser) {
        userRepository.save(currentUser);
    }

    @Override
    public User findUserByResetToken(String token) {
        return userRepository.findUserByResetToken(token);
    }

    /**
     * Создает сущность пользователя на основе данных, предоставленных объектом {@link UserDTO}.
     *
     * @param userDTO Объект передачи данных пользователя.
     * @return Сущность пользователя, созданная на основе данных из {@code userDTO}.
     */
    private User createUserFromDTO(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    /**
     * Ищет конкретную роль.
     * Если роль не найдена, создает новую роль, устанавливает имя "ROLE_USER"
     * и сохраняет ее в репозитории ролей.
     *
     * @return Конкретная роль
     */
    private Role getOrCreateUserRole() {
        Role role = roleRepository.findRoleByLogin("ROLE_USER");
        return (role != null) ? role : createAndSaveUserRole();
    }

    /**
     * Создает и сохраняет новую конкретную роль
     *
     * @return Новая роль
     */
    private Role createAndSaveUserRole() {
        Role role = new Role();
        role.setLogin("ROLE_USER");
        return roleRepository.save(role);
    }

    /**
     * Проверяет, что новый пароль отличается от предыдущего.
     *
     * @param currentUser Сущность пользователя, для которого изменяется пароль.
     * @param newPassword Новый пароль пользователя.
     * @throws IllegalArgumentException Если новый пароль совпадает с предыдущим.
     */
    private void validateNewPassword(User currentUser, String newPassword) {
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException(
                    "Новый пароль не должен совпадать со старым");
        }
    }
}
