package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProject;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovForbiddenException;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovProjectMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovProjectRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovProjectServiceImpl implements AblaySharimovProjectService {

    private final AblaySharimovProjectRepository projectRepository;
    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovProjectMapper projectMapper;

    @Override
    public AblaySharimovProjectResponse createProject(AblaySharimovCreateProjectRequest request, Long ownerId) {
        log.info("Creating project: {} for user: {}", request.getName(), ownerId);

        AblaySharimovUser owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Owner not found"));

        AblaySharimovProject project = AblaySharimovProject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .owner(owner)
                .build();

        AblaySharimovProject savedProject = projectRepository.save(project);
        log.info("Project created with id: {}", savedProject.getId());
        return projectMapper.toResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public AblaySharimovProjectDetailResponse getProjectById(Long id) {
        log.debug("Getting project by id: {}", id);
        AblaySharimovProject project = projectRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toDetailResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovProjectResponse> getAllProjects(Pageable pageable, String search, AblaySharimovProjectStatus status) {
        log.debug("Getting all projects");
        if (status != null) {
            return projectRepository.findByStatus(status, pageable).map(projectMapper::toResponse);
        }
        if (search != null) {
            return projectRepository.findByNameContainingIgnoreCase(search, pageable).map(projectMapper::toResponse);
        }
        return projectRepository.findAll(pageable).map(projectMapper::toResponse);
    }

    @Override
    public AblaySharimovProjectResponse updateProject(Long id, AblaySharimovUpdateProjectRequest request) {
        log.info("Updating project: {}", id);
        AblaySharimovProject project = projectRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found with id: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AblaySharimovUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Current user not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new AblaySharimovForbiddenException("You don't have permission to update this project");
        }

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getPriority() != null) project.setPriority(request.getPriority());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());

        AblaySharimovProject updatedProject = projectRepository.save(project);
        return projectMapper.toResponse(updatedProject);
    }

    @Override
    public void deleteProject(Long id) {
        log.info("Deleting project: {}", id);
        AblaySharimovProject project = projectRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found with id: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AblaySharimovUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Current user not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new AblaySharimovForbiddenException("You don't have permission to delete this project");
        }

        projectRepository.deleteById(id);
    }

    @Override
    public AblaySharimovProjectResponse addMemberToProject(Long projectId, Long userId) {
        log.info("Adding member {} to project {}", userId, projectId);
        AblaySharimovProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found"));

        AblaySharimovUser user = userRepository.findById(userId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        project.getMembers().add(user);
        AblaySharimovProject updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }

    @Override
    public AblaySharimovProjectResponse removeMemberFromProject(Long projectId, Long userId) {
        log.info("Removing member {} from project {}", userId, projectId);
        AblaySharimovProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Project not found"));

        AblaySharimovUser user = userRepository.findById(userId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        project.getMembers().remove(user);
        AblaySharimovProject updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AblaySharimovProjectResponse> getMyProjects(Long userId) {
        log.debug("Getting projects for user: {}", userId);
        return projectRepository.findByOwnerId(userId).stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }
}

