package com.app.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Media {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String path;
    @Setter
    private String fileName;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Override
    public String toString() {
        return "Media{" +
                "path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                '}';
    }
}
