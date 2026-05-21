package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovUserResponse {

    private Long id;

    private String username;

    private String email;

    private AblaySharimovRole role;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("profile_picture_path")
    private String profilePicturePath;
}

