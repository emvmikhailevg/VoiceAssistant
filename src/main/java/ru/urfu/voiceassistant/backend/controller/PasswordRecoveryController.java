package ru.urfu.voiceassistant.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.voiceassistant.backend.dto.PasswordRecoveryDTO;
import ru.urfu.voiceassistant.backend.database.model.User;
import ru.urfu.voiceassistant.backend.service.PasswordResetService;
import ru.urfu.voiceassistant.backend.service.UserService;

import java.util.Objects;

/**
 * Контроллер для сброса пароля.
 */
@Controller
public class PasswordRecoveryController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    /**
     * Конструктор контроллера.
     *
     * @param userService          Сервис для работы с пользователями.
     * @param passwordResetService Сервис сброса пароля.
     */
    @Autowired
    public PasswordRecoveryController(UserService userService,
                                    PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Отображение страницы сброса пароля.
     *
     * @return Страница "passwordResetPage".
     */
    @GetMapping("/password_reset")
    public String resetPassword() {
        return "passwordResetPage";
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
        User currentUser = userService.findUserByEmail(email);

        if (currentUser == null) {
            model.addAttribute("error", "Такого пользователя не существует");
            return "passwordResetPage";
        }

        String resetToken = passwordResetService.generateResetToken(currentUser);
        currentUser.setResetToken(resetToken);
        userService.saveUser(currentUser);

        passwordResetService.sendInstructionsToChangePassword(currentUser);

        model.addAttribute(
                "success",
                "Перейдите по ссылке на почте для смены пароля");

        return "passwordResetPage";
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
            model.addAttribute("error", "Такого токена не существует");
        }

        model.addAttribute("token", token);
        return "recoveryPasswordPage";
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
        User user = userService.findUserByResetToken(token);

        if (user == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            String validationErrorMessage =
                    Objects.requireNonNull(bindingResult.getFieldError("password")).getDefaultMessage();
            model.addAttribute("error", validationErrorMessage);
            return "recoveryPasswordPage";
        }

        if (!passwordRecoveryDTO.getPassword().equals(passwordRecoveryDTO.getConfirmPassword())) {
            model.addAttribute("error", "Пароли совпадают");
            return "recoveryPasswordPage";
        }

        try {
            userService.updateUserPassword(user, passwordRecoveryDTO.getPassword());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "recoveryPasswordPage";
        }
    }
}
