package com.example.faculty.repository;

import com.example.faculty.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {

    @Query("""
        SELECT DISTINCT e
        FROM Education e
        LEFT JOIN FETCH e.documents
        WHERE e.faculty.user.id = :userId
    """)
    List<Education> findByFacultyUserId(@Param("userId") Long userId);
    List<Education> findByFacultyId(Long facultyId);
=======

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByFaculty_Id(Long facultyProfileId);
    Optional<Education> findByIdAndFaculty_Id(Long id, Long facultyProfileId);
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
