package ru.urfu.voiceassistant.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Утилитарный класс для загрузки файла в виде ресурса.
 */
public class FileDownloadUtil {

    private Path foundFile;

    /**
     * Получает файл в виде ресурса по его коду.
     *
     * @param fileCode код файла.
     * @return файл в виде ресурса.
     * @throws IOException выбрасывается в случае ошибок ввода-вывода.
     */
    public Resource getFileAsResource(String fileCode) throws IOException {
        Path uploadDirectory = Paths.get("Files-Upload");

        Files.list(uploadDirectory).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}
