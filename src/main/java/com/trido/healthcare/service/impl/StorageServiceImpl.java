package com.trido.healthcare.service.impl;

import com.trido.healthcare.config.StorageProperties;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.exception.InvalidRequestException;
import com.trido.healthcare.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {
    private String location;

    @Autowired
    public StorageServiceImpl(StorageProperties storageProperties) {
        this.location = storageProperties.getLocation();
    }

    @Override
    public Path saveImage(MultipartFile file, UUID userId) {
        try {
            if (file.isEmpty()) {
                throw new IOException();
            }

            Path folder = Paths.get(this.location).resolve(userId.toString());
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            Path fileLocationPath = folder.resolve(Paths.get(file.getOriginalFilename()).normalize()).toAbsolutePath();
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, fileLocationPath, StandardCopyOption.REPLACE_EXISTING);
            return fileLocationPath;
        } catch (IOException e) {
            throw new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_REQUEST,
                    ConstantMessages.INVALID_UPLOADED_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Resource loadImage(String filename, UUID userId) throws IOException {
        Path folder = Paths.get(this.location).resolve(userId.toString());
        Path fileLocationPath = folder.resolve(filename).toAbsolutePath();
        if (!Files.exists(fileLocationPath)) {
            //Throw exceptions
            throw new IOException();
        }
        Resource fileData = new UrlResource(fileLocationPath.toUri());
        return fileData;
    }
}
