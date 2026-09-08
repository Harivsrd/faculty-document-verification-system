package com.example.faculty.service;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse uploadEducationCertificate(User currentUser, Long educationId, MultipartFile file);

    DocumentResponse uploadExperienceCertificate(User currentUser, Long experienceId, MultipartFile file);

    List<DocumentResponse> getMyDocuments(User currentUser);
}
