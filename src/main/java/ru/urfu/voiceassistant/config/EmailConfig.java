package ru.urfu.voiceassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Конфигурационный класс для настройки отправки электронных писем через {@link JavaMailSender}.
 */
@Configuration
public class EmailConfig {

    /**
     * Имя пользователя для подключения к почтовому серверу.
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * Пароль пользователя для подключения к почтовому серверу.
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Метод, создающий и настраивающий {@link JavaMailSender} для отправки электронных писем.
     *
     * @return {@link JavaMailSender} с настроенными параметрами.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
