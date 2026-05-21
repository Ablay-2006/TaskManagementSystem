package kz.ablaysharimov.taskmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovCreateProjectRequest {

    @NotBlank(message = "Project name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Status cannot be null")
    private AblaySharimovProjectStatus status;

    @NotNull(message = "Priority cannot be null")
    private AblaySharimovPriority priority;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}

