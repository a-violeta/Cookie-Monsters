package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
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

    public Media(){
        this.path = "";
        this.fileName = "";
        this.createdAt = LocalDateTime.now();
        this.type = MediaType.IMAGE;
    }

    public Media(String path, String fileName, MediaType type){
        this.path = path;
        this.fileName = fileName;
        this.createdAt = LocalDateTime.now();
        this.type = type;
    }
}