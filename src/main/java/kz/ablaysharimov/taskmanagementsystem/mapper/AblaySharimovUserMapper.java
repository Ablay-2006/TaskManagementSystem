package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovUserResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AblaySharimovUserMapper {

    public AblaySharimovUserResponse toResponse(AblaySharimovUser user) {
        if (user == null) {
            return null;
        }
        return AblaySharimovUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .isActive(user.getIsActive())
                .profilePicturePath(user.getProfilePicturePath())
                .build();
    }

    public List<AblaySharimovUserResponse> toResponseList(List<AblaySharimovUser> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

