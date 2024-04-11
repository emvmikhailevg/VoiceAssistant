package ru.urfu.voiceassistant.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

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
    @NotEmpty(message = "Имя файла не может быть пустым")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileUploadResponseDTO that = (FileUploadResponseDTO) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
