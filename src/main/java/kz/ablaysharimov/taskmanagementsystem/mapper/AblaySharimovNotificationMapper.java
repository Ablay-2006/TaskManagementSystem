package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovNotificationResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovNotification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AblaySharimovNotificationMapper {

    public AblaySharimovNotificationResponse toResponse(AblaySharimovNotification notification) {
        if (notification == null) {
            return null;
        }
        return AblaySharimovNotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .relatedTaskId(notification.getRelatedTaskId())
                .relatedProjectId(notification.getRelatedProjectId())
                .build();
    }

    public List<AblaySharimovNotificationResponse> toResponseList(List<AblaySharimovNotification> notifications) {
        if (notifications == null) {
            return null;
        }
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

