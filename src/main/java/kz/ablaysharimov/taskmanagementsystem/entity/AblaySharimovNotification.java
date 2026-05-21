package kz.ablaysharimov.taskmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ablaysharimov_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AblaySharimovNotificationType type;

    @Column(nullable = false)
    private Boolean isRead;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private AblaySharimovUser recipient;

    @Column(nullable = true)
    private Long relatedTaskId;

    @Column(nullable = true)
    private Long relatedProjectId;
}

