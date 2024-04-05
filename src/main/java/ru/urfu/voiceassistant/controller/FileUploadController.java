package ru.urfu.voiceassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.exception.UnsupportedFileTypeException;
import ru.urfu.voiceassistant.repository.FileRepository;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.service.FileService;
import ru.urfu.voiceassistant.util.enums.RedirectUrlNames;
import ru.urfu.voiceassistant.util.enums.ViewNames;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;

import static ru.urfu.voiceassistant.util.FileUploadUtil.deleteFileByCode;

/**
 * Контроллер для загрузки файлов.
 */
@Controller
@RequestMapping("/upload_file")
public class FileUploadController {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    /**
     * Конструктор контроллера.
     *
     * @param fileService    Сервис для работы с файлами.
     * @param userRepository  Репозиторий пользователей.
     * @param fileRepository Репозиторий файлов.
     */
    @Autowired
    public FileUploadController(FileService fileService,
                                UserRepository userRepository,
                                FileRepository fileRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * Обработка запроса на загрузку файла.
     *
     * @param multipartFile Загружаемый файл.
     * @param principal     Текущий пользователь.
     * @param model         Модель для передачи данных в представление.
     * @return Редирект на страницу загрузки файлов или страницу входа в случае ошибки аутентификации.
     * @throws IOException Исключение в случае ошибки ввода/вывода.
     */
    @PostMapping("")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             Principal principal,
                             Model model) throws IOException {
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        try {
            FileUploadResponseDTO newAudioFile = fileService.createNewAudioFile(multipartFile);
            fileService.saveFile(newAudioFile, uniqueUser);
            return "redirect:/%s".formatted(RedirectUrlNames.UPLOAD_FILE.getUrlAddress());
        } catch (UnsupportedFileTypeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("files", fileRepository.findFilesEntityByUserId(uniqueUser.getId()));
            model.addAttribute("user", uniqueUser.getLogin());

            return ViewNames.UPLOAD_FILE_PAGE.getName();
        }
    }

    /**
     * Получение страницы загрузки файлов.
     *
     * @param principal Текущий пользователь.
     * @return Модель и представление "uploadFile" со списком загруженных файлов.
     */
    @GetMapping("")
    public ModelAndView GetUploadFilePage(Principal principal) {
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        ModelAndView modelAndView = new ModelAndView(ViewNames.UPLOAD_FILE_PAGE.getName());
        ModelAndView modelAndViewLogin = new ModelAndView(ViewNames.LOGIN_PAGE.getName());

        if (uniqueUser == null) {
            return modelAndViewLogin;
        }

        modelAndView.addObject("files", fileRepository.findFilesEntityByUserId(uniqueUser.getId()));
        modelAndView.addObject("user", uniqueUser.getLogin());

        return modelAndView;
    }

    /**
     * Обработка запроса на удаление файла.
     *
     * @param fileId   Идентификатор файла.
     * @param principal Текущий пользователь.
     * @param model     Модель для передачи данных в представление.
     * @return Редирект на страницу загрузки файлов.
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId, Principal principal, Model model) {
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        try {
            String linkToDownload = fileRepository.findFileEntityById(fileId).getDownloadURL();
            String fileCode = linkToDownload.substring(linkToDownload.lastIndexOf("/") + 1);
            deleteFileByCode(fileCode);
            fileService.deleteFile(fileId, uniqueUser);
        } catch (FileNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/%s".formatted(RedirectUrlNames.UPLOAD_FILE.getUrlAddress());
    }
}
