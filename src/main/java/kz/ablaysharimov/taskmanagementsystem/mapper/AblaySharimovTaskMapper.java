package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovTaskResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AblaySharimovTaskMapper {

    private final AblaySharimovCommentMapper commentMapper;
    private final AblaySharimovAttachmentMapper attachmentMapper;

    public AblaySharimovTaskResponse toResponse(AblaySharimovTask task) {
        if (task == null) {
            return null;
        }
        return AblaySharimovTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .createdAt(task.getCreatedAt())
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .assigneeUsername(task.getAssignee() != null ? task.getAssignee().getUsername() : null)
                .createdByUsername(task.getCreatedBy() != null ? task.getCreatedBy().getUsername() : null)
                .commentsCount(task.getComments() != null ? task.getComments().size() : 0)
                .attachmentsCount(task.getAttachments() != null ? task.getAttachments().size() : 0)
                .build();
    }

    public AblaySharimovTaskDetailResponse toDetailResponse(AblaySharimovTask task) {
        if (task == null) {
            return null;
        }
        AblaySharimovTaskResponse baseResponse = toResponse(task);

        AblaySharimovTaskDetailResponse response = new AblaySharimovTaskDetailResponse();
        response.setId(baseResponse.getId());
        response.setTitle(baseResponse.getTitle());
        response.setDescription(baseResponse.getDescription());
        response.setStatus(baseResponse.getStatus());
        response.setPriority(baseResponse.getPriority());
        response.setDueDate(baseResponse.getDueDate());
        response.setEstimatedHours(baseResponse.getEstimatedHours());
        response.setActualHours(baseResponse.getActualHours());
        response.setCreatedAt(baseResponse.getCreatedAt());
        response.setProjectName(baseResponse.getProjectName());
        response.setAssigneeUsername(baseResponse.getAssigneeUsername());
        response.setCreatedByUsername(baseResponse.getCreatedByUsername());
        response.setCommentsCount(baseResponse.getCommentsCount());
        response.setAttachmentsCount(baseResponse.getAttachmentsCount());
        response.setComments(commentMapper.toResponseList(task.getComments()));
        response.setAttachments(attachmentMapper.toResponseList(task.getAttachments()));
        return response;
    }
}
