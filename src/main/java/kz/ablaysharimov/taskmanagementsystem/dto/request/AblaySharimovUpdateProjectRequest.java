package kz.ablaysharimov.taskmanagementsystem.dto.request;

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
public class AblaySharimovUpdateProjectRequest {

    private String name;

    private String description;

    private AblaySharimovProjectStatus status;

    private AblaySharimovPriority priority;

    private LocalDate startDate;

    private LocalDate endDate;
}

