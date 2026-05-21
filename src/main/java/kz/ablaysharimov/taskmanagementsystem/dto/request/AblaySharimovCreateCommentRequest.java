package kz.ablaysharimov.taskmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovCreateCommentRequest {

    @NotBlank(message = "Comment content cannot be blank")
    private String content;

    @NotNull(message = "Task ID cannot be null")
    private Long taskId;
}

