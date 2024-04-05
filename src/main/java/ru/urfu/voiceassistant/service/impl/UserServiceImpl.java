package ru.urfu.voiceassistant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.voiceassistant.dto.UserDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.entity.role.Role;
import ru.urfu.voiceassistant.repository.RoleRepository;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.service.EmailSenderService;
import ru.urfu.voiceassistant.service.UserService;
import ru.urfu.voiceassistant.util.enums.ExceptionMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструирует новый объект {@link UserServiceImpl} с указанными зависимостями.
     *
     * @param userRepository       Репозиторий пользователей.
     * @param roleRepository       Репозиторий ролей.
     * @param passwordEncoder      Кодировщик паролей.
     * @param emailSenderService   Сервис отправки электронных писем.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        UserEntity user = createUserEntityFromDTO(userDTO);
        Role role = getOrCreateUserRole();

        user.setRoles(List.of(role));

        String activationMessage = buildActivationMessage(user);
        emailSenderService.sender(user.getEmail(), "Код активации", activationMessage);

        userRepository.save(user);
    }

    @Override
    public boolean activateUser(String activationCode) {
        UserEntity currentUser = userRepository.findUserEntityByActivationCode(activationCode);
        return currentUser != null && currentUser.getActivationCode() != null;
    }

    @Override
    public void updateUserPassword(UserEntity currentUser, String newPassword) {
        validateNewPassword(currentUser, newPassword);

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        currentUser.setResetToken(null);

        userRepository.save(currentUser);
    }

    /**
     * Создает сущность пользователя на основе данных, предоставленных объектом {@link UserDTO}.
     *
     * @param userDTO Объект передачи данных пользователя.
     * @return Сущность пользователя, созданная на основе данных из {@code userDTO}.
     */
    private UserEntity createUserEntityFromDTO(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActivationCode(UUID.randomUUID().toString());
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
        Role role = roleRepository.findByLogin("ROLE_USER");
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
     * Строит текстовое сообщение для активации пользователя.
     *
     * @param user Сущность пользователя, для которого создается сообщение.
     * @return Текстовое сообщение с инструкциями по активации пользователя.
     */
    private String buildActivationMessage(UserEntity user) {
        return String.format(
                "Hello, %s. Now you can visit the next link: http://localhost:8080/activate/%s",
                user.getLogin(),
                user.getActivationCode()
        );
    }

    /**
     * Проверяет, что новый пароль отличается от предыдущего.
     *
     * @param currentUser Сущность пользователя, для которого изменяется пароль.
     * @param newPassword Новый пароль пользователя.
     * @throws IllegalArgumentException Если новый пароль совпадает с предыдущим.
     */
    private void validateNewPassword(UserEntity currentUser, String newPassword) {
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.NEW_PASSWORD_SHOULD_BE_DIFFERENT_FROM_OLD_PASSWORD.getMessage());
        }
    }
}
