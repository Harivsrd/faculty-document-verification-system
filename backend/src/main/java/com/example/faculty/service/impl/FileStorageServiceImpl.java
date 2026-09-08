package com.example.faculty.service.impl;

import com.example.faculty.exception.InvalidFileException;
import com.example.faculty.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${app.file.allowed-extensions}")
    private String allowedExtensionsRaw;

    @Value("${app.file.max-size-bytes}")
    private long maxSizeBytes;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf", "image/jpeg", "image/jpg", "image/png"
    );

    private Path rootLocation;

    private Path getRootLocation() {
        if (rootLocation == null) {
            rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(rootLocation);
            } catch (IOException e) {
                throw new IllegalStateException("Could not initialize upload storage directory", e);
            }
        }
        return rootLocation;
    }

    @Override
    public StoredFile store(MultipartFile file, Long facultyId, String category) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("No file was uploaded");
        }

        if (file.getSize() > maxSizeBytes) {
            throw new InvalidFileException("File exceeds the maximum allowed size of "
                    + (maxSizeBytes / (1024 * 1024)) + "MB");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");

        if (originalFilename.contains("..")) {
            throw new InvalidFileException("Invalid file name");
        }

        String extension = getExtension(originalFilename);
        List<String> allowedExtensions = List.of(allowedExtensionsRaw.toLowerCase().split(","));

        if (extension.isEmpty() || !allowedExtensions.contains(extension.toLowerCase())) {
            throw new InvalidFileException("Unsupported file type. Allowed types: " + allowedExtensionsRaw);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Unsupported content type: " + contentType);
        }

        // Never trust the original filename for the physical file name.
        String storedFileName = UUID.randomUUID() + "." + extension;

        String safeCategory = category.replaceAll("[^a-zA-Z0-9_-]", "");
        Path facultyDir = getRootLocation()
                .resolve("faculty-" + facultyId)
                .resolve(safeCategory)
                .normalize();

        // Defend against path traversal: resolved dir must stay under root.
        if (!facultyDir.startsWith(getRootLocation())) {
            throw new InvalidFileException("Invalid storage path");
        }

        try {
            Files.createDirectories(facultyDir);
            Path destination = facultyDir.resolve(storedFileName).normalize();

            if (!destination.startsWith(facultyDir)) {
                throw new InvalidFileException("Invalid storage path");
            }

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            Path relative = getRootLocation().relativize(destination);
            return new StoredFile(storedFileName, relative.toString());
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new InvalidFileException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public Path resolve(String relativeFilePath) {
        Path resolved = getRootLocation().resolve(relativeFilePath).normalize();
        if (!resolved.startsWith(getRootLocation())) {
            throw new InvalidFileException("Invalid file path");
        }
        return resolved;
    }

    @Override
    public void delete(String relativeFilePath) {
        try {
            Files.deleteIfExists(resolve(relativeFilePath));
        } catch (IOException e) {
            log.warn("Failed to delete file {}: {}", relativeFilePath, e.getMessage());
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1);
    }
}
