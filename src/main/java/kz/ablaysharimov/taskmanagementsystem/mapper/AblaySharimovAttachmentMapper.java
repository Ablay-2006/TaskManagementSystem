package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAttachmentResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovAttachment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AblaySharimovAttachmentMapper {

    public AblaySharimovAttachmentResponse toResponse(AblaySharimovAttachment attachment) {
        if (attachment == null) {
            return null;
        }
        return AblaySharimovAttachmentResponse.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .originalFileName(attachment.getOriginalFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .uploadedAt(attachment.getUploadedAt())
                .uploadedByUsername(attachment.getUploadedBy() != null ? attachment.getUploadedBy().getUsername() : null)
                .downloadUrl("/api/files/download/" + attachment.getId())
                .build();
    }

    public List<AblaySharimovAttachmentResponse> toResponseList(List<AblaySharimovAttachment> attachments) {
        if (attachments == null) {
            return null;
        }
        return attachments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

