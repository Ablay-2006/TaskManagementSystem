package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateTaskRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovTaskService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Tasks", description = "Task management endpoints")
public class AblaySharimovTaskController {

    private final AblaySharimovTaskService taskService;
    private final AblaySharimovUserService userService;

    @PostMapping
    @Operation(summary = "Create task", description = "Create a new task in a project")
    public ResponseEntity<AblaySharimovTaskResponse> createTask(@Valid @RequestBody AblaySharimovCreateTaskRequest request) {
        log.info("Creating task: {}", request.getTitle());
        Long userId = userService.getCurrentUser().getId();
        AblaySharimovTaskResponse task = taskService.createTask(request, userId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get tasks", description = "Get paginated, filtered, and searchable tasks")
    public ResponseEntity<Page<AblaySharimovTaskResponse>> getTasks(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) AblaySharimovTaskStatus status,
            @RequestParam(required = false) AblaySharimovPriority priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDateTime dueDateFrom,
            @RequestParam(required = false) LocalDateTime dueDateTo,
            Pageable pageable) {
        log.info("Getting tasks with filters");
        Page<AblaySharimovTaskResponse> tasks = taskService.getTasks(projectId, status, priority, assigneeId, search, dueDateFrom, dueDateTo, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve task details with comments and attachments")
    public ResponseEntity<AblaySharimovTaskDetailResponse> getTaskById(@PathVariable Long id) {
        log.info("Getting task by id: {}", id);
        AblaySharimovTaskDetailResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Update task details")
    public ResponseEntity<AblaySharimovTaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody AblaySharimovUpdateTaskRequest request) {
        log.info("Updating task: {}", id);
        AblaySharimovTaskResponse task = taskService.updateTask(id, request);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task: {}", id);
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change task status", description = "Change status of a task")
    public ResponseEntity<AblaySharimovTaskResponse> changeTaskStatus(
            @PathVariable Long id,
            @RequestParam AblaySharimovTaskStatus newStatus) {
        log.info("Changing task {} status to {}", id, newStatus);
        AblaySharimovTaskResponse task = taskService.changeTaskStatus(id, newStatus);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign task", description = "Assign task to a user")
    public ResponseEntity<AblaySharimovTaskResponse> assignTask(
            @PathVariable Long id,
            @RequestParam Long userId) {
        log.info("Assigning task {} to user {}", id, userId);
        AblaySharimovTaskResponse task = taskService.assignTask(id, userId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/my")
    @Operation(summary = "Get my tasks", description = "Get all tasks assigned to current user")
    public ResponseEntity<Page<AblaySharimovTaskResponse>> getMyTasks(Pageable pageable) {
        log.info("Getting current user's tasks");
        Long userId = userService.getCurrentUser().getId();
        Page<AblaySharimovTaskResponse> tasks = taskService.getMyTasks(userId, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue tasks", description = "Get all overdue tasks asynchronously")
    public CompletableFuture<ResponseEntity<List<AblaySharimovTaskResponse>>> getOverdueTasks() {
        log.info("Getting overdue tasks");
        return taskService.getOverdueTasks()
                .thenApply(tasks -> ResponseEntity.ok(tasks));
    }

    @GetMapping("/stats/project/{projectId}")
    @Operation(summary = "Get project task stats", description = "Get task statistics for a project asynchronously")
    public CompletableFuture<ResponseEntity<Map<String, Long>>> getProjectStats(@PathVariable Long projectId) {
        log.info("Getting project stats for project: {}", projectId);
        return taskService.getTaskStatsByProject(projectId)
                .thenApply(ResponseEntity::ok);
    }
}

