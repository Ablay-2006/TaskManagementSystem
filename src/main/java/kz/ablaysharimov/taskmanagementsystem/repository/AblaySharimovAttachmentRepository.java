package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AblaySharimovAttachmentRepository extends JpaRepository<AblaySharimovAttachment, Long> {
    List<AblaySharimovAttachment> findByTaskId(Long taskId);
    void deleteByTaskId(Long taskId);
}

