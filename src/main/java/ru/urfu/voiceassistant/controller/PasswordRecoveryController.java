package ru.urfu.voiceassistant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.voiceassistant.dto.PasswordRecoveryDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.service.PasswordResetService;
import ru.urfu.voiceassistant.service.UserService;
import ru.urfu.voiceassistant.util.enums.ModelMessageAttribute;
import ru.urfu.voiceassistant.util.enums.RedirectUrlNames;
import ru.urfu.voiceassistant.util.enums.ValidMessage;
import ru.urfu.voiceassistant.util.enums.ViewNames;

import java.util.Objects;

/**
 * Контроллер для сброса пароля.
 */
@Controller
public class PasswordRecoveryController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;

    /**
     * Конструктор контроллера.
     *
     * @param userService         Сервис для работы с пользователями.
     * @param userRepository      Репозиторий пользователей.
     * @param passwordResetService Сервис сброса пароля.
     */
    @Autowired
    public PasswordRecoveryController(UserService userService,
                                    UserRepository userRepository,
                                    PasswordResetService passwordResetService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Отображение страницы сброса пароля.
     *
     * @return Страница "passwordResetPage".
     */
    @GetMapping("/password_reset")
    public String resetPassword() {
        return ViewNames.PASSWORD_RESET_PAGE.getName();
    }

    /**
     * Обработка запроса на сброс пароля.
     *
     * @param email Адрес электронной почты пользователя.
     * @param model Модель для передачи данных в представление.
     * @return Возвращает страницу "passwordResetPage" с сообщением об успешной отправке инструкции.
     */
    @PostMapping("/password_reset")
    public String resetPassword(@RequestParam String email, Model model) {
        UserEntity currentUser = userRepository.findByEmail(email);

        if (currentUser == null) {
            model.addAttribute("error", ModelMessageAttribute.USER_DOES_NOT_EXIST_MESSAGE.getMessage());
            return ViewNames.PASSWORD_RESET_PAGE.getName();
        }

        String resetToken = passwordResetService.generateResetToken(currentUser);
        currentUser.setResetToken(resetToken);
        userRepository.save(currentUser);

        passwordResetService.sendInstructionsToChangePassword(currentUser);

        model.addAttribute(
                "success",
                ModelMessageAttribute.INSTRUCTIONS_TO_RESET_PASSWORD_MESSAGE.getMessage());

        return ViewNames.PASSWORD_RESET_PAGE.getName();
    }

    /**
     * Отображение страницы восстановления пароля.
     *
     * @param token Токен сброса пароля.
     * @param model Модель для передачи данных в представление.
     * @return Страница "recoveryPasswordPage".
     */
    @GetMapping("/password_recovery/{token}")
    public String showPasswordRecoveryPage(@PathVariable String token, Model model) {
        if (token == null) {
            model.addAttribute("error", ModelMessageAttribute.TOKEN_NOT_EXIST.getMessage());
        }

        model.addAttribute("token", token);
        return ViewNames.RECOVERY_PASSWORD_PAGE.getName();
    }

    /**
     * Обработка запроса на восстановление пароля.
     *
     * @param token               Токен сброса пароля.
     * @param passwordRecoveryDTO DTO объект для смены пароля
     * @param bindingResult       Результат валидации данных.
     * @param model               Модель для передачи данных в представление.
     * @return Возвращает страницу "recoveryPasswordPage" с сообщением об успешной смене пароля или ошибкой.
     */
    @PostMapping("/password_recovery/{token}")
    public String recoverPassword(@PathVariable String token,
                                  @Valid @ModelAttribute PasswordRecoveryDTO passwordRecoveryDTO,
                                  BindingResult bindingResult,
                                  Model model) {
        UserEntity user = userRepository.findUserEntityByResetToken(token);

        if (user == null) {
            return "redirect:/%s".formatted(RedirectUrlNames.LOGIN.getUrlAddress());
        }

        if (bindingResult.hasErrors()) {
            String validationErrorMessage =
                    Objects.requireNonNull(bindingResult.getFieldError("password")).getDefaultMessage();
            model.addAttribute("error", validationErrorMessage);
            return ViewNames.RECOVERY_PASSWORD_PAGE.getName();
        }

        if (!passwordRecoveryDTO.getPassword().equals(passwordRecoveryDTO.getConfirmPassword())) {
            model.addAttribute("error", ValidMessage.PASSWORDS_ARE_DIFFERENT.getMessage());
            return ViewNames.RECOVERY_PASSWORD_PAGE.getName();
        }

        try {
            userService.updateUserPassword(user, passwordRecoveryDTO.getPassword());
            return "redirect:/%s?success".formatted(RedirectUrlNames.LOGIN.getUrlAddress());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return ViewNames.RECOVERY_PASSWORD_PAGE.getName();
        }
    }
}
