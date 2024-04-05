package ru.urfu.voiceassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.voiceassistant.entity.FileEntity;

import java.util.List;

/**
 * Интерфейс репозитория для взаимодействия с таблицей файлов в базе данных.
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * Находит список файлов по идентификатору пользователя.
     *
     * @param id идентификатор пользователя.
     * @return список файлов пользователя.
     */
    List<FileEntity> findFilesEntityByUserId(Long id);

    /**
     * Находит файл по его идентификатору.
     *
     * @param id идентификатор файла.
     * @return файл с указанным идентификатором.
     */
    FileEntity findFileEntityById(Long id);

    /**
     * Удаляет файл по его идентификатору.
     *
     * @param id идентификатор файла.
     */
    void deleteById(Long id);
}
