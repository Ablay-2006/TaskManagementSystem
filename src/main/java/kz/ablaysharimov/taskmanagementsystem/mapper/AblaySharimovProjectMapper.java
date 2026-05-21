package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AblaySharimovProjectMapper {

    private final AblaySharimovUserMapper userMapper;

    public AblaySharimovProjectResponse toResponse(AblaySharimovProject project) {
        if (project == null) {
            return null;
        }
        return AblaySharimovProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .priority(project.getPriority())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .createdAt(project.getCreatedAt())
                .ownerUsername(project.getOwner() != null ? project.getOwner().getUsername() : null)
                .memberCount(project.getMembers() != null ? project.getMembers().size() : 0)
                .build();
    }

    public AblaySharimovProjectDetailResponse toDetailResponse(AblaySharimovProject project) {
        if (project == null) {
            return null;
        }
        AblaySharimovProjectResponse baseResponse = toResponse(project);

        AblaySharimovProjectDetailResponse response = new AblaySharimovProjectDetailResponse();
        response.setId(baseResponse.getId());
        response.setName(baseResponse.getName());
        response.setDescription(baseResponse.getDescription());
        response.setStatus(baseResponse.getStatus());
        response.setPriority(baseResponse.getPriority());
        response.setStartDate(baseResponse.getStartDate());
        response.setEndDate(baseResponse.getEndDate());
        response.setCreatedAt(baseResponse.getCreatedAt());
        response.setOwnerUsername(baseResponse.getOwnerUsername());
        response.setMemberCount(baseResponse.getMemberCount());
        response.setMembers(userMapper.toResponseList(project.getMembers().stream().toList()));
        response.setTasksSummary("Tasks: " + (project.getId()));
        return response;
    }
}
