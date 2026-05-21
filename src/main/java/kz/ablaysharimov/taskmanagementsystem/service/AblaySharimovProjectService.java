package kz.ablaysharimov.taskmanagementsystem.service;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AblaySharimovProjectService {
    AblaySharimovProjectResponse createProject(AblaySharimovCreateProjectRequest request, Long ownerId);
    AblaySharimovProjectDetailResponse getProjectById(Long id);
    Page<AblaySharimovProjectResponse> getAllProjects(Pageable pageable, String search, AblaySharimovProjectStatus status);
    AblaySharimovProjectResponse updateProject(Long id, AblaySharimovUpdateProjectRequest request);
    void deleteProject(Long id);
    AblaySharimovProjectResponse addMemberToProject(Long projectId, Long userId);
    AblaySharimovProjectResponse removeMemberFromProject(Long projectId, Long userId);
    List<AblaySharimovProjectResponse> getMyProjects(Long userId);
}

