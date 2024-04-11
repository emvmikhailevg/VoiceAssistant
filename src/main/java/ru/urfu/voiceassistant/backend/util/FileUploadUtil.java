package ru.urfu.voiceassistant.backend.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Утилитарный класс для загрузки и удаления файлов.
 */
public class FileUploadUtil {

    /**
     * Сохраняет файл на сервере и возвращает его уникальный код.
     *
     * @param fileName      имя файла.
     * @param multipartFile объект {@link MultipartFile} с данными файла.
     * @return уникальный код файла.
     * @throws IOException выбрасывается в случае ошибок ввода-вывода.
     */
    public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadDirectory = Paths.get("files-upload");

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadDirectory.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Не удалось сохранить файл: "
                    + fileName, e);
        }

        return fileCode;
    }

    /**
     * Удаляет файл по его коду.
     *
     * @param fileCode уникальный код файла.
     * @throws IOException выбрасывается в случае ошибок ввода-вывода.
     */
    public static void deleteFileByCode(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                uploadDirectory,
                fileCode + "*")) {
            for (Path file : directoryStream) {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new IOException("Не удалось удалить файл: "
                    + fileCode, e);
        }
    }
}
