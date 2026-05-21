package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovAuthResponse {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("token_type")
    private String tokenType;

    private String username;

    private String email;

    private AblaySharimovRole role;

    @JsonProperty("expires_in")
    private Long expiresIn;
}

