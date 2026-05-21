package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovNotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovNotificationResponse {

    private Long id;

    private String message;

    private AblaySharimovNotificationType type;

    @JsonProperty("is_read")
    private Boolean isRead;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("related_task_id")
    private Long relatedTaskId;

    @JsonProperty("related_project_id")
    private Long relatedProjectId;
}

