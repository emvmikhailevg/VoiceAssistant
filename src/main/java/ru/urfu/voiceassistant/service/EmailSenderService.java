package ru.urfu.voiceassistant.service;

/**
 * Сервис, предоставляющий функционал для операции отправки электронных писем.
 */
public interface EmailSenderService {

    /**
     * Отправляет электронное письмо с заданными параметрами.
     *
     * @param email   Адрес получателя письма.
     * @param subject Тема письма.
     * @param message Текстовое сообщение письма.
     */
    void sender(String email, String subject, String message);
}
