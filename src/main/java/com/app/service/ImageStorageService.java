package com.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${app.storage.s3.enabled:false}")
    private boolean isS3Enabled;

    @Value("${app.storage.s3.bucket-name:my-reddit-bucket}")
    private String bucketName;

    @Value("${app.storage.local.dir:uploads/}")
    private String localUploadDir;

    @Value("${app.storage.local.base-url:http://localhost:8080/uploads/}")
    private String localBaseUrl;

    public String saveImage(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        try {
            if (isS3Enabled) {
                return saveToS3(file, uniqueFileName);
            } else {
                return saveLocally(file, uniqueFileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    private String saveLocally(MultipartFile file, String fileName) throws IOException {
        Path uploadPath = Paths.get(localUploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return localBaseUrl + fileName;
    }

    private String saveToS3(MultipartFile file, String fileName) throws IOException {
        try (S3Client s3Client = S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://" + bucketName + ".s3." + s3Client.serviceClientConfiguration().region().id() + ".amazonaws.com/" + fileName;
        }
    }
}
