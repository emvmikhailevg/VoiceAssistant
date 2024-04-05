package ru.urfu.voiceassistant.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс DTO (Data Transfer Object) для представления ответа на запрос о загрузке файла.
 * Содержит информацию о файле, включая его имя, размер, URL для скачивания и дату создания.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseDTO {

    /**
     * Имя файла. Не должно быть пустым.
     */
    @NotEmpty(message = "File name cannot be empty")
    private String fileName;

    /**
     * Размер файла.
     */
    private Double size;

    /**
     * URL для скачивания файла. Должен существовать.
     */
    @NotNull(message = "URL для скачивания должен существовать")
    private String downloadURL;

    /**
     * Дата и время создания файла.
     */
    private LocalDateTime createdAt;
}
