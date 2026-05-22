package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AblaySharimovTaskRepository extends JpaRepository<AblaySharimovTask, Long>, JpaSpecificationExecutor<AblaySharimovTask> {
    Page<AblaySharimovTask> findByProjectId(Long projectId, Pageable pageable);
    Page<AblaySharimovTask> findByAssigneeId(Long assigneeId, Pageable pageable);
    List<AblaySharimovTask> findByDueDateBefore(LocalDateTime date);

    @Query("SELECT t FROM AblaySharimovTask t WHERE (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AblaySharimovTask> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}

