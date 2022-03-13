package com.trido.healthcare.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface StorageService {
    Path saveImage(MultipartFile file, UUID userId);

    Resource loadImage(String filename, UUID userId) throws IOException;
}
