package ru.urfu.voiceassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.voiceassistant.dto.FileUploadResponseDTO;
import ru.urfu.voiceassistant.entity.UserEntity;
import ru.urfu.voiceassistant.repository.FileRepository;
import ru.urfu.voiceassistant.repository.UserRepository;
import ru.urfu.voiceassistant.service.FileService;
import ru.urfu.voiceassistant.util.FileUploadUtil;
import ru.urfu.voiceassistant.util.enums.RedirectUrlNames;
import ru.urfu.voiceassistant.util.enums.ViewNames;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;

/**
 * Контроллер для работы с записями файлов.
 */
@Controller
@RequestMapping("/record")
public class FileRecordController {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    /**
     * Конструктор контроллера.
     *
     * @param fileService       Сервис для работы с файлами.
     * @param userRepository    Репозиторий пользователей.
     * @param fileRepository    Репозиторий файлов.
     */
    @Autowired
    public FileRecordController(FileService fileService,
                                UserRepository userRepository,
                                FileRepository fileRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * Получение основной страницы для работы с записями файлов.
     *
     * @return Модель и представление "recordFilePage" со списком файлов.
     */
    @GetMapping("")
    public ModelAndView getMainRecordPage() {
        ModelAndView mainPage = new ModelAndView(ViewNames.RECORD_FILE_PAGE.getName());
        mainPage.addObject("files", fileRepository.findAll());
        return mainPage;
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
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        FileUploadResponseDTO newAudioFile = fileService.createNewAudioFile(multipartFile);
        fileService.saveFile(newAudioFile, uniqueUser);

        return "redirect:/%s".formatted(RedirectUrlNames.RECORD.getUrlAddress());
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
        UserEntity uniqueUser = userRepository.findByEmail(principal.getName());

        try {
            String linkToDownload = fileRepository.findFileEntityById(fileId).getDownloadURL();
            String fileCode = linkToDownload.substring(linkToDownload.lastIndexOf("/") + 1);
            FileUploadUtil.deleteFileByCode(fileCode);
            fileService.deleteFile(fileId, uniqueUser);
        } catch (FileNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/%s".formatted(RedirectUrlNames.RECORD.getUrlAddress());
    }
}
