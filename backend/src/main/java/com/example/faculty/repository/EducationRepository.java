package com.example.faculty.repository;

import com.example.faculty.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
