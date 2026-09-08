package com.example.faculty.repository;

import com.example.faculty.entity.FacultyProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FacultyProfileRepository extends JpaRepository<FacultyProfile, Long> {

    Optional<FacultyProfile> findByUserId(Long userId);

    Optional<FacultyProfile> findByUserEmail(String email);

    @Query("SELECT f FROM FacultyProfile f WHERE " +
            "(:keyword IS NULL OR LOWER(f.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.specialization) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.department) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<FacultyProfile> search(@Param("keyword") String keyword, Pageable pageable);
}
