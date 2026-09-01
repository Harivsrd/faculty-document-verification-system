package com.example.faculty.repository;

import com.example.faculty.entity.FacultyProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FacultyProfileRepository extends JpaRepository<FacultyProfile, Long> {

    Optional<FacultyProfile> findByUser_Id(Long userId);

    @Query("""
           SELECT f FROM FacultyProfile f
           WHERE (:search IS NULL OR :search = ''
                  OR LOWER(f.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(f.user.email) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(f.specialization) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(f.department) LIKE LOWER(CONCAT('%', :search, '%')))
           """)
    Page<FacultyProfile> search(@Param("search") String search, Pageable pageable);

    Page<FacultyProfile> findByPubliclyVisibleTrue(Pageable pageable);
}
