package kz.ablaysharimov.taskmanagementsystem.service;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AblaySharimovTaskService {
    AblaySharimovTaskResponse createTask(AblaySharimovCreateTaskRequest request, Long createdById);
    AblaySharimovTaskDetailResponse getTaskById(Long id);
    Page<AblaySharimovTaskResponse> getTasks(Long projectId, AblaySharimovTaskStatus status, AblaySharimovPriority priority,
                                               Long assigneeId, String search, LocalDateTime dueDateFrom, LocalDateTime dueDateTo, Pageable pageable);
    AblaySharimovTaskResponse updateTask(Long id, AblaySharimovUpdateTaskRequest request);
    void deleteTask(Long id);
    AblaySharimovTaskResponse changeTaskStatus(Long id, AblaySharimovTaskStatus newStatus);
    AblaySharimovTaskResponse assignTask(Long taskId, Long userId);
    Page<AblaySharimovTaskResponse> getMyTasks(Long userId, Pageable pageable);
    CompletableFuture<List<AblaySharimovTaskResponse>> getOverdueTasks();
    CompletableFuture<Map<String, Long>> getTaskStatsByProject(Long projectId);
}

