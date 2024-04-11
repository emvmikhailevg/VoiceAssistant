package ru.urfu.voiceassistant.backend.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.backend.database.model.File;

import java.util.List;

/**
 * Интерфейс репозитория для взаимодействия с таблицей файлов в базе данных.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Находит список файлов по идентификатору пользователя.
     *
     * @param id идентификатор пользователя.
     * @return список файлов пользователя.
     */
    List<File> findFilesByUserId(Long id);

    /**
     * Находит файл по его идентификатору.
     *
     * @param id идентификатор файла.
     * @return файл с указанным идентификатором.
     */
    File findFileById(Long id);

    /**
     * Удаляет файл по его идентификатору.
     *
     * @param id идентификатор файла.
     */
    void deleteFileById(Long id);
}
