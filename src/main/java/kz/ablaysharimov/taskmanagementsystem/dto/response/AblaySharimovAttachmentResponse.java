package kz.ablaysharimov.taskmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AblaySharimovAttachmentResponse {

    private Long id;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("original_file_name")
    private String originalFileName;

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("uploaded_at")
    private LocalDateTime uploadedAt;

    @JsonProperty("uploaded_by_username")
    private String uploadedByUsername;

    @JsonProperty("download_url")
    private String downloadUrl;
}

