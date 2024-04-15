package ru.urfu.voiceassistant.backend.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс представляет сущность файла в системе.
 */
@Entity
@Table(name = "app_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {

    /**
     * Идентификатор файла.
     */
    @Id
    @SequenceGenerator(name = "app_file_id_seq", sequenceName = "app_file_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_file_id_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Имя файла.
     */
    @NotBlank
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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    /**
     * Пользователь, которому принадлежит файл.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
