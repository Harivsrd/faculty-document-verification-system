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
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
<<<<<<< HEAD
    @Column(nullable = false, length = 30)
=======
    @Column(nullable = false)
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    private AuditAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

<<<<<<< HEAD
    @Column(updatable = false)
    private LocalDateTime timestamp;

    @Column(length = 500)
    private String description;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
=======
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;

    @Column(length = 1000)
    private String description;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
