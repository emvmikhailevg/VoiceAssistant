package ru.urfu.voiceassistant.service;

import org.springframework.web.multipart.MultipartFile;
import ru.urfu.voiceassistant.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.entity.UserEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Сервис, предоставляющий функционал операций с файлами.
 * Этот сервис обеспечивает сохранение файлов, поиск файлов пользователя по идентификатору,
 * удаление файлов с проверкой разрешений, и создание нового аудиофайла с сохранением информации в базе данных.
 */
public interface FileService {

    /**
     * Сохраняет файл в базе данных.
     *
     * @param fileUploadResponseDTO Информация о загруженном файле.
     * @param user Пользователь, которому принадлежит файл.
     */
    void saveFile(FileUploadResponseDTO fileUploadResponseDTO, UserEntity user);

    /**
     * Удаляет файл по его идентификатору с проверкой разрешений пользователя.
     *
     * @param fileId Идентификатор файла.
     * @param user Пользователь, выполняющий удаление.
     * @throws FileNotFoundException Если файл не найден по заданному идентификатору.
     */
    void deleteFile(Long fileId, UserEntity user) throws IOException;

    /**
     * Создает новый аудиофайл, сохраняет его и возвращает информацию о загруженном файле.
     *
     * @param multipartFile Загружаемый аудиофайл.
     * @return Информация о загруженном аудиофайле.
     * @throws IOException Если произошла ошибка при работе с файлом.
     */
    FileUploadResponseDTO createNewAudioFile(MultipartFile multipartFile) throws IOException;
}
