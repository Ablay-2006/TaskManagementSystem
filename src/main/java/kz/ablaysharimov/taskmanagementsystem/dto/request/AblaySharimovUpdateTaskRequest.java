package kz.ablaysharimov.taskmanagementsystem.dto.request;

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
public class AblaySharimovUpdateTaskRequest {

    private String title;

    private String description;

    private AblaySharimovTaskStatus status;

    private AblaySharimovPriority priority;

    private LocalDateTime dueDate;

    private Double estimatedHours;

    private Double actualHours;

    private Long assigneeId;
}

