package kz.ablaysharimov.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovCreateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovUpdateProjectRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectDetailResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovProjectResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovProjectService;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Projects", description = "Project management endpoints")
public class AblaySharimovProjectController {

    private final AblaySharimovProjectService projectService;
    private final AblaySharimovUserService userService;

    @PostMapping
    @Operation(summary = "Create project", description = "Create a new project")
    public ResponseEntity<AblaySharimovProjectResponse> createProject(@Valid @RequestBody AblaySharimovCreateProjectRequest request) {
        log.info("Creating project: {}", request.getName());
        Long userId = userService.getCurrentUser().getId();
        AblaySharimovProjectResponse project = projectService.createProject(request, userId);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Get paginated list of projects")
    public ResponseEntity<Page<AblaySharimovProjectResponse>> getAllProjects(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AblaySharimovProjectStatus status) {
        log.info("Getting all projects");
        Page<AblaySharimovProjectResponse> projects = projectService.getAllProjects(pageable, search, status);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve project details including members")
    public ResponseEntity<AblaySharimovProjectDetailResponse> getProjectById(@PathVariable Long id) {
        log.info("Getting project by id: {}", id);
        AblaySharimovProjectDetailResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update project details")
    public ResponseEntity<AblaySharimovProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody AblaySharimovUpdateProjectRequest request) {
        log.info("Updating project: {}", id);
        AblaySharimovProjectResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete project", description = "Delete a project")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.info("Deleting project: {}", id);
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/members/{userId}")
    @Operation(summary = "Add member to project", description = "Add a user as project member")
    public ResponseEntity<AblaySharimovProjectResponse> addMemberToProject(
            @PathVariable Long id,
            @PathVariable Long userId) {
        log.info("Adding member {} to project {}", userId, id);
        AblaySharimovProjectResponse project = projectService.addMemberToProject(id, userId);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "Remove member from project", description = "Remove a user from project members")
    public ResponseEntity<AblaySharimovProjectResponse> removeMemberFromProject(
            @PathVariable Long id,
            @PathVariable Long userId) {
        log.info("Removing member {} from project {}", userId, id);
        AblaySharimovProjectResponse project = projectService.removeMemberFromProject(id, userId);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/my")
    @Operation(summary = "Get my projects", description = "Get all projects owned by current user")
    public ResponseEntity<List<AblaySharimovProjectResponse>> getMyProjects() {
        log.info("Getting current user's projects");
        Long userId = userService.getCurrentUser().getId();
        List<AblaySharimovProjectResponse> projects = projectService.getMyProjects(userId);
        return ResponseEntity.ok(projects);
    }
}

