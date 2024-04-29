package ru.urfu.voiceassistant.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.backend.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.backend.database.model.File;
import ru.urfu.voiceassistant.backend.database.model.User;
import ru.urfu.voiceassistant.backend.service.FileService;
import ru.urfu.voiceassistant.backend.service.UserService;
import ru.urfu.voiceassistant.backend.util.FileUploadUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Контроллер для работы с записями файлов.
 */
@Controller
@RequestMapping("/record")
public class FileRecordController {


    private final UserService userService;
    private final FileService fileService;

    /**
     * Конструктор контроллера.
     *
     * @param userService Сервис для работы с пользователями.
     * @param fileService Сервис для работы с файлами.
     */
    @Autowired
    public FileRecordController(UserService userService,
                                FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    /**
     * Получение основной страницы для работы с записями файлов.
     *
     * @return Модель и представление "recordFilePage" со списком файлов.
     */
    @GetMapping("")
    public ModelAndView getMainRecordPage(Principal principal) {
        User uniqueUser = userService.findUserByEmail(principal.getName());

        ModelAndView mainPage = new ModelAndView("recordFilePage");
        List<File> files = fileService.findAllFilesByUserId(uniqueUser.getId());
        mainPage.addObject("files", files);
        return mainPage;
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
     * Обработка запроса на сохранение записи о файле.
     *
     * @param multipartFile Загружаемый файл.
     * @param principal     Текущий пользователь.
     * @return Редирект на основную страницу работы с записями файлов.
     * @throws IOException Исключение в случае ошибки ввода/вывода.
     */
    @PostMapping("/save")
    public String saveRecord(MultipartFile multipartFile, Principal principal) throws IOException {
        User uniqueUser = userService.findUserByEmail(principal.getName());

        FileUploadResponseDTO newAudioFile = fileService.createNewAudioFile(multipartFile);
        fileService.saveFile(newAudioFile, uniqueUser);

        return "redirect:/record";
    }

    /**
     * Обработка запроса на удаление файла.
     *
     * @param fileId    Идентификатор файла.
     * @param principal Текущий пользователь.
     * @param model     Модель для передачи данных в представление.
     * @return Редирект на основную страницу работы с записями файлов.
     */
    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId, Principal principal, Model model) {
        User uniqueUser = userService.findUserByEmail(principal.getName());

        try {
            String linkToDownload = fileService.findFileById(fileId).getDownloadURL();
            String fileCode = linkToDownload.substring(linkToDownload.lastIndexOf("/") + 1);
            FileUploadUtil.deleteFileByCode(fileCode);
            fileService.deleteFile(fileId, uniqueUser);
        } catch (FileNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/record";
    }
}
