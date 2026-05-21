package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AblaySharimovCommentRepository extends JpaRepository<AblaySharimovComment, Long> {
    Page<AblaySharimovComment> findByTaskIdOrderByCreatedAtDesc(Long taskId, Pageable pageable);
}

