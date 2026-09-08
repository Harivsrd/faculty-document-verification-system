package com.example.faculty.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    /**
     * Validates and stores an uploaded file under a per-faculty, per-category
     * directory, using a generated (non-guessable) filename. Returns the
     * relative storage path (not exposed to clients) and generated filename.
     */
    StoredFile store(MultipartFile file, Long facultyId, String category);

    Path resolve(String relativeFilePath);

    void delete(String relativeFilePath);

    record StoredFile(String storedFileName, String relativeFilePath) {
    }
}
