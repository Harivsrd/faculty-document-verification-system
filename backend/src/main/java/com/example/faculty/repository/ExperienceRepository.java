package com.example.faculty.repository;

import com.example.faculty.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByFaculty_Id(Long facultyProfileId);
    Optional<Experience> findByIdAndFaculty_Id(Long id, Long facultyProfileId);
}
