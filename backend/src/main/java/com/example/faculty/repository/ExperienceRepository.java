package com.example.faculty.repository;

import com.example.faculty.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByFacultyId(Long facultyId);
    List<Experience> findByFacultyUserId(Long userId);
}
