package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AblaySharimovTaskRepository extends JpaRepository<AblaySharimovTask, Long> {
    Page<AblaySharimovTask> findByProjectId(Long projectId, Pageable pageable);
    Page<AblaySharimovTask> findByAssigneeId(Long assigneeId, Pageable pageable);
    Page<AblaySharimovTask> findByStatus(AblaySharimovTaskStatus status, Pageable pageable);
    List<AblaySharimovTask> findByPriority(AblaySharimovPriority priority);
    List<AblaySharimovTask> findByDueDateBefore(LocalDateTime date);

    @Query("SELECT t FROM AblaySharimovTask t WHERE (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AblaySharimovTask> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM AblaySharimovTask t WHERE " +
            "(:projectId IS NULL OR t.project.id = :projectId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
            "(:dueDateFrom IS NULL OR t.dueDate >= :dueDateFrom) AND " +
            "(:dueDateTo IS NULL OR t.dueDate <= :dueDateTo)")
    Page<AblaySharimovTask> filterTasks(
            @Param("projectId") Long projectId,
            @Param("status") AblaySharimovTaskStatus status,
            @Param("priority") AblaySharimovPriority priority,
            @Param("assigneeId") Long assigneeId,
            @Param("dueDateFrom") LocalDateTime dueDateFrom,
            @Param("dueDateTo") LocalDateTime dueDateTo,
            Pageable pageable);
}

