package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AblaySharimovTaskResponse {

    private Long id;

    private String title;

    private String description;

    private AblaySharimovTaskStatus status;

    private AblaySharimovPriority priority;

    @JsonProperty("due_date")
    private LocalDateTime dueDate;

    @JsonProperty("estimated_hours")
    private Double estimatedHours;

    @JsonProperty("actual_hours")
    private Double actualHours;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("assignee_username")
    private String assigneeUsername;

    @JsonProperty("created_by_username")
    private String createdByUsername;

    @JsonProperty("comments_count")
    private Integer commentsCount;

    @JsonProperty("attachments_count")
    private Integer attachmentsCount;
}

