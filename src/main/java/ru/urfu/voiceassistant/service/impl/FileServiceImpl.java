package ru.urfu.voiceassistant.service.impl;

import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.voiceassistant.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.database.model.File;
import ru.urfu.voiceassistant.database.model.User;
import ru.urfu.voiceassistant.database.repository.FileRepository;
import ru.urfu.voiceassistant.service.FileService;
import ru.urfu.voiceassistant.util.FileUploadUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Реализация интерфейса {@link FileService}.
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    /**
     * Конструирует новый объект {@code FileServiceImpl} с указанным репозиторием файлов.
     *
     * @param fileRepository Репозиторий файлов.
     */
    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void saveFile(FileUploadResponseDTO fileUploadResponseDTO, User user) {
        File file = createFileEntity(fileUploadResponseDTO, user);
        fileRepository.save(file);
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId, User user) throws FileNotFoundException {
        File file = getFileById(fileId);
        checkUserPermission(file, user);
        fileRepository.deleteFileById(fileId);
    }

    /**
     * Создает новый аудиофайл, сохраняет его и возвращает информацию о загруженном файле.
     *
     * @param multipartFile Загружаемый аудиофайл.
     * @return Информация о загруженном аудиофайле.
     * @throws IOException Если произошла ошибка при работе с файлом.
     */
    @Override
    public FileUploadResponseDTO createNewAudioFile(MultipartFile multipartFile) throws IOException {
        validateAudioFileType(multipartFile);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Double size = calculateFileSizeInMB(multipartFile);
        String fileCode = FileUploadUtil.saveFile(fileName, multipartFile);

        FileUploadResponseDTO fileUploadResponseDTO = new FileUploadResponseDTO();
        fileUploadResponseDTO.setFileName(fileName);
        fileUploadResponseDTO.setSize(size);
        fileUploadResponseDTO.setDownloadURL("/download_file/" + fileCode);

        return fileUploadResponseDTO;
    }

    @Override
    public List<File> findAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public File findFileById(Long fileId) {
        return fileRepository.findFileById(fileId);
    }

    @Override
    public List<File> findAllFilesByUserId(Long id) {
        return fileRepository.findFilesByUserId(id);
    }

    /**
     * Создает сущность файла на основе информации о загруженном файле и пользователя.
     *
     * @param fileUploadResponseDTO Информация о загруженном файле.
     * @param user Пользователь, которому принадлежит файл.
     * @return Сущность файла.
     */
    private File createFileEntity(FileUploadResponseDTO fileUploadResponseDTO, User user) {
        File file = new File();
        file.setFileName(fileUploadResponseDTO.getFileName());
        file.setSize(fileUploadResponseDTO.getSize());
        file.setDownloadURL(fileUploadResponseDTO.getDownloadURL());
        file.setUser(user);
        return file;
    }

    /**
     * Получает файл по его идентификатору или выбрасывает исключение, если файл не найден.
     *
     * @param fileId Идентификатор файла.
     * @return Сущность файла.
     * @throws FileNotFoundException Если файл не найден по заданному идентификатору.
     */
    private File getFileById(Long fileId) throws FileNotFoundException {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(
                        "Файл с данным id не найден: " + fileId));
    }

    /**
     * Проверяет разрешения пользователя на удаление файла.
     *
     * @param file Сущность файла.
     * @param user Пользователь, выполняющий удаление.
     * @throws AccessDeniedException Если пользователь не имеет прав на удаление файла.
     */
    private void checkUserPermission(File file, User user) {
        if (!file.getUser().equals(user)) {
            throw new AccessDeniedException("У вас нет прав на удаление файла");
        }
    }

    /**
     * Проверяет тип аудиофайла и выбрасывает исключение, если файл не поддерживается.
     *
     * @param multipartFile Загружаемый аудиофайл.
     * @throws RuntimeException Если тип файла не поддерживается.
     */
    private void validateAudioFileType(MultipartFile multipartFile) {
        String fileExtension = FilenameUtils.getExtension(
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
        );

        if (!("mp3".equalsIgnoreCase(fileExtension) || "wav".equalsIgnoreCase(fileExtension))) {
            throw new RuntimeException(
                    "Неподдерживаемый тип файла"
            );
        }
    }

    /**
     * Рассчитывает размер файла в мегабайтах.
     *
     * @param multipartFile Загружаемый файл.
     * @return Размер файла в мегабайтах.
     */
    private Double calculateFileSizeInMB(MultipartFile multipartFile) {
        return Math.round((multipartFile.getSize() / Math.pow(2, 20)) * 10.0) / 10.0;
    }
}
