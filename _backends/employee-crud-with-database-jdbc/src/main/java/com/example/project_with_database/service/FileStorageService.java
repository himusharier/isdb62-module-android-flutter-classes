package com.example.project_with_database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorageService {
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private ResourceLoader resourceLoader;

    public String storeFile(MultipartFile file) throws IOException {
        String randomFileName = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
                + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path path = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return randomFileName;
    }

}
