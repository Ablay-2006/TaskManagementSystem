package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProject;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovTaskMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovProjectRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovTaskRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovNotificationService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovTaskServiceImpl implements AblaySharimovTaskService {

    private final AblaySharimovTaskRepository taskRepository;
    private final AblaySharimovProjectRepository projectRepository;
    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovTaskMapper taskMapper;
    private final AblaySharimovNotificationService notificationService;

    @Override
    public AblaySharimovTaskResponse createTask(AblaySharimovCreateTaskRequest request, Long createdById) {
        log.info("Creating task: {} for project: {}", request.getTitle(), request.getProjectId());

        AblaySharimovProject project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found"));

        AblaySharimovUser createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        AblaySharimovUser assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Assignee not found"));
        }

        AblaySharimovTask task = AblaySharimovTask.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .estimatedHours(request.getEstimatedHours())
                .project(project)
                .assignee(assignee)
                .createdBy(createdBy)
                .build();

        AblaySharimovTask savedTask = taskRepository.save(task);
        log.info("Task created with id: {}", savedTask.getId());

        if (assignee != null) {
            notificationService.sendTaskAssignmentNotification(savedTask, assignee);
        }

        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public AblaySharimovTaskDetailResponse getTaskById(Long id) {
        log.debug("Getting task by id: {}", id);
        AblaySharimovTask task = taskRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDetailResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovTaskResponse> getTasks(Long projectId, AblaySharimovTaskStatus status, AblaySharimovPriority priority,
                                                     Long assigneeId, String search, LocalDateTime dueDateFrom, LocalDateTime dueDateTo, Pageable pageable) {
        log.debug("Getting tasks with filters");

        if (search != null && !search.isEmpty()) {
            return taskRepository.searchByKeyword(search, pageable).map(taskMapper::toResponse);
        }

        return taskRepository.filterTasks(projectId, status, priority, assigneeId, dueDateFrom, dueDateTo, pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    public AblaySharimovTaskResponse updateTask(Long id, AblaySharimovUpdateTaskRequest request) {
        log.info("Updating task: {}", id);
        AblaySharimovTask task = taskRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
        if (request.getActualHours() != null) task.setActualHours(request.getActualHours());

        if (request.getAssigneeId() != null) {
            AblaySharimovUser assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        AblaySharimovTask updated = taskRepository.save(task);
        notificationService.sendTaskUpdateNotification(updated);
        return taskMapper.toResponse(updated);
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task: {}", id);
        AblaySharimovTask task = taskRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found with id: " + id));
        taskRepository.deleteById(id);
    }

    @Override
    public AblaySharimovTaskResponse changeTaskStatus(Long id, AblaySharimovTaskStatus newStatus) {
        log.info("Changing task {} status to {}", id, newStatus);
        AblaySharimovTask task = taskRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found with id: " + id));
        task.setStatus(newStatus);
        AblaySharimovTask updated = taskRepository.save(task);
        return taskMapper.toResponse(updated);
    }

    @Override
    public AblaySharimovTaskResponse assignTask(Long taskId, Long userId) {
        log.info("Assigning task {} to user {}", taskId, userId);
        AblaySharimovTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found"));

        AblaySharimovUser user = userRepository.findById(userId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        task.setAssignee(user);
        AblaySharimovTask updated = taskRepository.save(task);
        notificationService.sendTaskAssignmentNotification(updated, user);
        return taskMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovTaskResponse> getMyTasks(Long userId, Pageable pageable) {
        log.debug("Getting tasks for user: {}", userId);
        return taskRepository.findByAssigneeId(userId, pageable).map(taskMapper::toResponse);
    }

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<AblaySharimovTaskResponse>> getOverdueTasks() {
        log.info("Getting overdue tasks");
        LocalDateTime now = LocalDateTime.now();
        List<AblaySharimovTaskResponse> overdueTasks = taskRepository.findByDueDateBefore(now).stream()
                .filter(task -> !task.getStatus().equals(AblaySharimovTaskStatus.DONE) &&
                               !task.getStatus().equals(AblaySharimovTaskStatus.CANCELLED))
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(overdueTasks);
    }

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Map<String, Long>> getTaskStatsByProject(Long projectId) {
        log.info("Getting task stats for project: {}", projectId);
        projectRepository.findById(projectId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found"));

        List<AblaySharimovTask> projectTasks = taskRepository.findByProjectId(projectId,
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) projectTasks.size());
        stats.put("completed", projectTasks.stream().filter(t -> t.getStatus() == AblaySharimovTaskStatus.DONE).count());
        stats.put("in_progress", projectTasks.stream().filter(t -> t.getStatus() == AblaySharimovTaskStatus.IN_PROGRESS).count());
        stats.put("todo", projectTasks.stream().filter(t -> t.getStatus() == AblaySharimovTaskStatus.TODO).count());

        return CompletableFuture.completedFuture(stats);
    }
}

