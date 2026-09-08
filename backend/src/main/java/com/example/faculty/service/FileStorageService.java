package com.example.faculty.service;

<<<<<<< HEAD
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
=======
import com.example.faculty.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private final Path rootLocation;
    private final Set<String> allowedExtensions;
    private final long maxFileSizeBytes;

    public FileStorageService(@Value("${app.file.upload-dir}") String uploadDir,
                               @Value("${app.file.allowed-extensions}") String allowedExtensionsCsv,
                               @Value("${app.file.max-file-size-bytes}") long maxFileSizeBytes) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.allowedExtensions = Set.of(allowedExtensionsCsv.toLowerCase().split(","));
        this.maxFileSizeBytes = maxFileSizeBytes;

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize upload storage directory", e);
        }
    }

    /**
     * Stores a validated file under uploads/faculty-{facultyId}/{category}/{uuid}.{ext}
     * and returns storage metadata. Never trusts the original filename for the physical path.
     */
    public StoredFile store(MultipartFile file, Long facultyId, String category) {
        validate(file);

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = getExtension(originalName);

        String storedFileName = UUID.randomUUID() + "." + extension;

        Path facultyDir = rootLocation.resolve("faculty-" + facultyId).resolve(category).normalize();

        // Path traversal guard: resolved directory must remain inside the root upload location.
        if (!facultyDir.startsWith(rootLocation)) {
            throw new InvalidFileException("Invalid storage path");
        }

        try {
            Files.createDirectories(facultyDir);
            Path destination = facultyDir.resolve(storedFileName).normalize();

            if (!destination.startsWith(facultyDir)) {
                throw new InvalidFileException("Invalid destination path");
            }

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            Path relativePath = rootLocation.relativize(destination);

            return new StoredFile(
                    originalName,
                    storedFileName,
                    relativePath.toString().replace("\\", "/"),
                    file.getContentType() != null ? file.getContentType() : "application/octet-stream",
                    file.getSize()
            );
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new InvalidFileException("Failed to store file: " + e.getMessage());
        }
    }

    /** Resolves a stored relative path back to an absolute path, guarding against traversal. */
    public Path resolve(String relativeFilePath) {
        Path resolved = rootLocation.resolve(relativeFilePath).normalize();
        if (!resolved.startsWith(rootLocation)) {
            throw new InvalidFileException("Invalid file path");
        }
        if (!Files.exists(resolved)) {
            throw new InvalidFileException("Stored file no longer exists on disk");
        }
        return resolved;
    }

    public void delete(String relativeFilePath) {
        try {
            Path resolved = rootLocation.resolve(relativeFilePath).normalize();
            if (!resolved.startsWith(rootLocation)) {
                throw new InvalidFileException("Invalid file path");
            }
            Files.deleteIfExists(resolved);
        } catch (IOException e) {
            log.warn("Failed to delete file {}: {}", relativeFilePath, e.getMessage());
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("No file was provided");
        }
        if (file.getSize() > maxFileSizeBytes) {
            throw new InvalidFileException("File exceeds the maximum allowed size");
        }

        String originalName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalName)) {
            throw new InvalidFileException("File name is missing");
        }
        if (originalName.contains("..")) {
            throw new InvalidFileException("Invalid file name");
        }

        String extension = getExtension(originalName);
        if (!allowedExtensions.contains(extension)) {
            throw new InvalidFileException(
                    "Unsupported file type. Allowed types: " + String.join(", ", allowedExtensions));
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new InvalidFileException("File must have a valid extension");
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    public record StoredFile(String originalFileName, String storedFileName, String relativePath,
                              String contentType, long size) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
