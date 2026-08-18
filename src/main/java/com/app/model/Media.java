package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "media")
public class Media {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String fileName;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private MediaType type;

    private Integer filter;

    public Media() {
        this.path = "";
        this.fileName = "";
        this.createdAt = LocalDateTime.now();
        this.type = MediaType.IMAGE;
        this.filter = null;
    }

    public Media(String path, String fileName, MediaType type, Integer filter) {
        this.path = path;
        this.fileName = fileName;
        this.createdAt = LocalDateTime.now();
        this.type = type;
        this.filter = filter;
    }
}