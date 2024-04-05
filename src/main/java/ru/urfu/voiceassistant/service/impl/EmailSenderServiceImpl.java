package ru.urfu.voiceassistant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.urfu.voiceassistant.service.EmailSenderService;

/**
 * Реализация интерфейса {@link EmailSenderService}.
 */
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sender(String email, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();

        emailMessage.setFrom(username);
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        emailSender.send(emailMessage);
    }
}
