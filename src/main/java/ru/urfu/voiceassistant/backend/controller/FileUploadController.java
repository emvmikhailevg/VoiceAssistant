package ru.urfu.voiceassistant.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.backend.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.backend.database.model.User;
import ru.urfu.voiceassistant.backend.service.FileService;
import ru.urfu.voiceassistant.backend.service.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;

import static ru.urfu.voiceassistant.backend.util.FileUploadUtil.deleteFileByCode;

/**
 * Контроллер для загрузки файлов.
 */
@Controller
@RequestMapping("/upload_file")
public class FileUploadController {

    private final FileService fileService;
    private final UserService userService;

    /**
     * Конструктор контроллера.
     *
     * @param fileService Сервис для работы с файлами.
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public FileUploadController(FileService fileService,
                                UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    /**
     * Обработчик для отправки запроса на обработку файла.
     *
     * @param fileId Идентификатор файла, для которого требуется обработка
     * @return Объект ResponseEntity с сообщением об успешной отправке запроса
     */
    @GetMapping("/send-request")
    @ResponseBody
    public ResponseEntity<String> sendRequest(@RequestParam("fileId") Long fileId) {
        fileService.sendRequestWithAudioData(fileId);
        return ResponseEntity.ok("Запрос успешно отправлен");
    }

    /**
     * Обработка запроса на загрузку файла.
     *
     * @param multipartFile Загружаемый файл.
     * @param principal     Текущий пользователь.
     * @param model         Модель для передачи данных в представление.
     * @return              Редирект на страницу загрузки файлов или страницу входа в случае ошибки аутентификации.
     * @throws IOException  Исключение в случае ошибки ввода/вывода.
     */
    @PostMapping("")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             Principal principal,
                             Model model) throws IOException {
        User uniqueUser = userService.findUserByEmail(principal.getName());

        try {
            FileUploadResponseDTO newAudioFile = fileService.createNewAudioFile(multipartFile);
            fileService.saveFile(newAudioFile, uniqueUser);
            return "redirect:/upload_file";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("files", fileService.findAllFilesByUserId(uniqueUser.getId()));
            model.addAttribute("user", uniqueUser.getLogin());

            return "uploadFile";
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
        User uniqueUser = userService.findUserByEmail(principal.getName());

        ModelAndView modelAndView = new ModelAndView("uploadFile");
        ModelAndView modelAndViewLogin = new ModelAndView("login");

        if (uniqueUser == null) {
            return modelAndViewLogin;
        }

        modelAndView.addObject("files", fileService.findAllFilesByUserId(uniqueUser.getId()));
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
        User uniqueUser = userService.findUserByEmail(principal.getName());

        try {
            String linkToDownload = fileService.findFileById(fileId).getDownloadURL();
            String fileCode = linkToDownload.substring(linkToDownload.lastIndexOf("/") + 1);
            deleteFileByCode(fileCode);
            fileService.deleteFile(fileId, uniqueUser);
        } catch (FileNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/upload_file";
    }
}
