package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovPriority;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovProjectResponse {

    private Long id;

    private String name;

    private String description;

    private AblaySharimovProjectStatus status;

    private AblaySharimovPriority priority;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("owner_username")
    private String ownerUsername;

    @JsonProperty("member_count")
    private Integer memberCount;
}

