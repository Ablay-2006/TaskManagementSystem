package kz.ablaysharimov.taskmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovCreateTaskRequest {

    @NotBlank(message = "Task title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Status cannot be null")
    private AblaySharimovTaskStatus status;

    @NotNull(message = "Priority cannot be null")
    private AblaySharimovPriority priority;

    private LocalDateTime dueDate;

    private Double estimatedHours;

    @NotNull(message = "Project ID cannot be null")
    private Long projectId;

    private Long assigneeId;
}

