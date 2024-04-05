package ru.urfu.voiceassistant.exception;

/**
 * Класс исключения, который вызывается, если пользователем загружает
 * файл неверного типа
 */
public class UnsupportedFileTypeException extends RuntimeException {

    /**
     * Конструктор класса
     *
     * @param message Сообщение, передаваемое пользователю
     */
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
