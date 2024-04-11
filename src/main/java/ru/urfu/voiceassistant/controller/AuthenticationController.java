package ru.urfu.voiceassistant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.dto.UserDTO;
import ru.urfu.voiceassistant.database.model.User;
import ru.urfu.voiceassistant.service.UserService;

/**
 * Контроллер для авторизации и управления аккаунтами.
 */
@Controller
public class AuthenticationController {

    private final UserService userService;

    /**
     * Конструктор контроллера.
     *
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение домашней страницы.
     *
     * @return Страница "index".
     */
    @GetMapping("/")
    public ModelAndView getHomePage() {
        return new ModelAndView("index");
    }

    /**
     * Получение страницы входа в систему.
     *
     * @return Страница "login".
     */
    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }

    /**
     * Отображение формы регистрации.
     *
     * @return Страница "register" с формой регистрации.
     */
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
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
        User uniqueUser = userService.findUserByEmail(userDTO.getEmail());

        if (uniqueUser != null && uniqueUser.getEmail() != null && !uniqueUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "Такой аккаунт уже существует");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "register";
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            model.addAttribute("error", "Пароли совпадают");
            return "register";
        }

        userService.registerUser(userDTO);
        return "redirect:/register";
    }
}
