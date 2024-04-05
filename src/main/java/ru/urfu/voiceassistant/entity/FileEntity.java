package ru.urfu.voiceassistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Класс представляет сущность файла в системе.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity {

    /**
     * Идентификатор файла.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Имя файла.
     */
    private String fileName;

    /**
     * Размер файла.
     */
    private Double size;

    /**
     * URL для скачивания файла.
     */
    private String downloadURL;

    /**
     * Дата и время создания файла. Генерируется автоматически при создании записи.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Пользователь, которому принадлежит файл.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
