package ru.urfu.voiceassistant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.dto.UserDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.util.enums.RedirectUrlNames;
import ru.urfu.voiceassistant.util.enums.ViewNames;

import java.security.Principal;

/**
 * Контроллер для управления персональной страницей пользователя.
 */
@Controller
@RequestMapping("/personal_page")
public class PersonalPageController {

    private final UserRepository userRepository;

    /**
     * Конструктор контроллера.
     *
     * @param userRepository Репозиторий пользователей.
     */
    @Autowired
    public PersonalPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получение персональной страницы пользователя.
     *
     * @param principal Текущий пользователь.
     * @return Модель и представление "personalPage" с данными пользователя.
     */
    @GetMapping("")
    public ModelAndView getPersonalPage(Principal principal) {
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        ModelAndView modelAndViewPersonalPage = new ModelAndView(ViewNames.PERSONAL_PAGE.getName());
        ModelAndView modelAndViewLogin = new ModelAndView(ViewNames.LOGIN_PAGE.getName());

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
    @PostMapping("/update")
    public String updatePersonalInfo(@Valid @ModelAttribute UserDTO userDTO,
                                     BindingResult bindingResult,
                                     Principal principal,
                                     Model model) {
        model.addAttribute("userDAO", userDTO);

        if (bindingResult.hasErrors() && userDTO.getPassword() != null) {
            return ViewNames.PERSONAL_PAGE.getName();
        }

        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());
        uniqueUser.setName(userDTO.getName());
        uniqueUser.setSurname(userDTO.getSurname());
        uniqueUser.setBirthday(userDTO.getBirthday());
        uniqueUser.setNumber(userDTO.getNumber());

        userRepository.save(uniqueUser);

        return "redirect:/%s".formatted(RedirectUrlNames.PERSONAL_PAGE.getUrlAddress());
    }
}
