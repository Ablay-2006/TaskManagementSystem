package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AblaySharimovNotificationRepository extends JpaRepository<AblaySharimovNotification, Long> {
    List<AblaySharimovNotification> findByRecipientIdAndIsReadFalse(Long userId);
    Page<AblaySharimovNotification> findByRecipientId(Long userId, Pageable pageable);
    long countByRecipientIdAndIsReadFalse(Long userId);
}

