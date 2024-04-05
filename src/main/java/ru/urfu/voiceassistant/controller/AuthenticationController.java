package ru.urfu.voiceassistant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.urfu.voiceassistant.dto.UserDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.service.UserService;
import ru.urfu.voiceassistant.util.enums.ModelMessageAttribute;
import ru.urfu.voiceassistant.util.enums.RedirectUrlNames;
import ru.urfu.voiceassistant.util.enums.ValidMessage;
import ru.urfu.voiceassistant.util.enums.ViewNames;

/**
 * Контроллер для авторизации и управления аккаунтами.
 */
@Controller
public class AuthenticationController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Конструктор контроллера.
     *
     * @param userService         Сервис для работы с пользователями.
     * @param userRepository      Репозиторий пользователя
     */
    @Autowired
    public AuthenticationController(UserService userService,
                                    UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Получение домашней страницы.
     *
     * @return Страница "index".
     */
    @GetMapping("/")
    public String getHomePage() { return ViewNames.INDEX_PAGE.getName(); }

    /**
     * Получение страницы входа в систему.
     *
     * @return Страница "login".
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return ViewNames.LOGIN_PAGE.getName();
    }

    /**
     * Отображение формы регистрации.
     *
     * @param model Модель для передачи данных в представление.
     * @return Страница "register" с формой регистрации.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return ViewNames.REGISTER_PAGE.getName();
    }

    /**
     * Обработка запроса на регистрацию нового пользователя.
     *
     * @param userDTO Данные нового пользователя.
     * @param result  Результат валидации данных.
     * @param model   Модель для передачи данных в представление.
     * @return Возвращает страницу "register" с сообщением об успешной регистрации или ошибкой.
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        UserEntity uniqueUser = userRepository.findByEmail(userDTO.getEmail());

        if (uniqueUser != null && uniqueUser.getEmail() != null && !uniqueUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, ValidMessage.ACCOUNT_EXISTS.getMessage());
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDTO);
            return ViewNames.REGISTER_PAGE.getName();
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            model.addAttribute("error", ValidMessage.PASSWORDS_ARE_DIFFERENT.getMessage());
            return ViewNames.REGISTER_PAGE.getName();
        }

        userService.registerUser(userDTO);
        return "redirect:/%s?success".formatted(RedirectUrlNames.REGISTER.getUrlAddress());
    }

    /**
     * Активация пользователя по коду активации.
     *
     * @param activationCode Код активации.
     * @param model          Модель для передачи данных в представление.
     * @return Возвращает страницу "login" с сообщением об успешной активации или ошибкой.
     */
    @GetMapping("/activate/{activationCode}")
    public String activationPage(@PathVariable String activationCode, Model model) {
        boolean isActivated = userService.activateUser(activationCode);

        if (isActivated) {
            model.addAttribute("message",
                    ModelMessageAttribute.USER_ACTIVATED_MESSAGE.getMessage());
        } else {
            model.addAttribute("message",
                    ModelMessageAttribute.USER_NOT_ACTIVATED_MESSAGE.getMessage());
        }

        return ViewNames.LOGIN_PAGE.getName();
    }
}
