package com.example.faculty.service;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.User;

public interface AuditLogService {
    void log(User user, AuditAction action, Document document, String description);
}
