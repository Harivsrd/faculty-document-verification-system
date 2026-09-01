package com.example.faculty.repository;

import com.example.faculty.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByFaculty_Id(Long facultyProfileId);
    Optional<Education> findByIdAndFaculty_Id(Long id, Long facultyProfileId);
}
