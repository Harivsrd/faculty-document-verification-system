package com.example.faculty.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD

=======
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyProfile faculty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QualificationType qualificationType;

    @Column(nullable = false)
    private String institution;

    private String boardOrUniversity;

    private String courseName;

    private Integer yearOfPassing;

    private Double percentageOrCgpa;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
=======
    @JoinColumn(name = "faculty_profile_id", nullable = false)
    private FacultyProfile faculty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualificationType qualificationType;

    private String institution;
    private String boardOrUniversity;
    private String courseName;
    private Integer yearOfPassing;
    private BigDecimal percentageOrCgpa;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Document> certificateDocuments = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
