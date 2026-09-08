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
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
=======
    /** The admin who performed the action. Null for system-generated transitions like re-upload resets. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    private DocumentStatus newStatus;

    @Column(length = 1000)
    private String comment;

<<<<<<< HEAD
    @Column(updatable = false)
    private LocalDateTime actionTimestamp;

    @PrePersist
    protected void onCreate() {
        actionTimestamp = LocalDateTime.now();
    }
=======
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime actionTimestamp;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
