package com.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Media {

    private static long idIncrementor = 0;
    // for id uniqueness, ids given will be 1, then 2, 3 ...

    @EqualsAndHashCode.Include
    private final long mediaId;
    @Setter
    private String path;
    @Setter
    private String fileName;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private MediaType type;

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    public Media() {
        this.mediaId=incrementId();
        this.path="";
        this.fileName="";
        this.createdAt=LocalDateTime.now();
        this.type=MediaType.IMAGE; // the default
    }

    public Media(String path, String fileName, LocalDateTime createdAt, MediaType mediaType) {
        this.mediaId=incrementId();
        this.path=path;
        this.fileName=fileName;
        this.createdAt=createdAt;
        this.type=mediaType;
    }

    @Override
    public String toString() {
        return "Media{" +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                '}';
    }
}
