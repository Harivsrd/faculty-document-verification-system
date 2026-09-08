package com.example.faculty.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD

import java.time.LocalDateTime;

=======
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents metadata about an uploaded certificate/document.
 * The actual binary file lives on the server filesystem; only metadata is stored here.
 */
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyProfile faculty;

=======
    @JoinColumn(name = "faculty_profile_id", nullable = false)
    private FacultyProfile faculty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    /** Optional link when this document is an education certificate. */
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id")
    private Education education;

<<<<<<< HEAD
=======
    /** Optional link when this document is an experience certificate. */
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

<<<<<<< HEAD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentType documentType;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, unique = true)
    private String storedFileName;

    @Column(nullable = false)
    private String filePath;

    private String contentType;

    private long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.PENDING;

=======
    /** Original filename as uploaded by the user (display only, never used as the physical path). */
    @Column(nullable = false)
    private String fileName;

    /** UUID-based physical filename on disk. */
    @Column(nullable = false, unique = true)
    private String storedFileName;

    /** Relative path on the server filesystem, e.g. uploads/faculty-3/education/uuid.pdf */
    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private DocumentStatus status = DocumentStatus.PENDING;

    @CreationTimestamp
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "verified_by")
=======
    @JoinColumn(name = "verified_by_user_id")
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    private User verifiedBy;

    @Column(length = 1000)
    private String rejectionReason;
<<<<<<< HEAD

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
=======
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
