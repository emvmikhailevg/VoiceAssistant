package ru.urfu.voiceassistant.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Контроллер для перехвата при переходе на несуществующий URL-адрес.
 */
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * Получение страницы ошибки 404 NOT FOUND.
     *
     * @return Страница "error".
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
