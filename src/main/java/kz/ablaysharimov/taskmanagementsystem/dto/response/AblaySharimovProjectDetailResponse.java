package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AblaySharimovProjectDetailResponse extends AblaySharimovProjectResponse {

    private List<AblaySharimovUserResponse> members;

    @JsonProperty("tasks_summary")
    private String tasksSummary;
}

