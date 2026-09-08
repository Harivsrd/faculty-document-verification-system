package com.example.faculty.repository;

import com.example.faculty.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
<<<<<<< HEAD

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByFacultyId(Long facultyId);
    List<Experience> findByFacultyUserId(Long userId);
=======
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByFaculty_Id(Long facultyProfileId);
    Optional<Experience> findByIdAndFaculty_Id(Long id, Long facultyProfileId);
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
