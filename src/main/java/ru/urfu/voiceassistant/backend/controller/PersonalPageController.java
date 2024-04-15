package ru.urfu.voiceassistant.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.backend.dto.UserDTO;
import ru.urfu.voiceassistant.backend.database.model.User;
import ru.urfu.voiceassistant.backend.service.UserService;

import java.security.Principal;

/**
 * Контроллер для управления персональной страницей пользователя.
 */
@Controller
@RequestMapping("/personal_page")
public class PersonalPageController {

    private final UserService userService;

    /**
     * Конструктор контроллера.
     *
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public PersonalPageController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение персональной страницы пользователя.
     *
     * @param principal Текущий пользователь.
     * @return Модель и представление "personalPage" с данными пользователя.
     */
    @GetMapping("")
    public ModelAndView getPersonalPage(Principal principal) {
        User uniqueUser = userService.findUserByEmail(principal.getName());

        ModelAndView modelAndViewPersonalPage = new ModelAndView("personalPage");
        ModelAndView modelAndViewLogin = new ModelAndView("login");

        modelAndViewPersonalPage.addObject("user", uniqueUser);

        if (uniqueUser == null) {
            return modelAndViewLogin;
        }

        return modelAndViewPersonalPage;
    }

    /**
     * Обработка запроса на обновление персональной информации пользователя.
     *
     * @param userDTO        DTO с обновленными данными пользователя.
     * @param bindingResult  Результат валидации данных.
     * @param principal      Текущий пользователь.
     * @param model          Модель для передачи данных в представление.
     * @return Редирект на персональную страницу пользователя или страницу ввода данных в случае ошибки.
     */
    @PostMapping("")
    public String updatePersonalInfo(@Valid @ModelAttribute UserDTO userDTO,
                                     BindingResult bindingResult,
                                     Principal principal,
                                     Model model) {
        model.addAttribute("userDAO", userDTO);

        if (bindingResult.hasErrors() && userDTO.getPassword() != null) {
            return "personalPage";
        }

        User uniqueUser = userService.findUserByEmail(principal.getName());
        uniqueUser.setName(userDTO.getName());
        uniqueUser.setSurname(userDTO.getSurname());
        uniqueUser.setBirthday(userDTO.getBirthday());
        uniqueUser.setNumber(userDTO.getNumber());

        userService.saveUser(uniqueUser);

        return "redirect:/personal_page";
    }
}
