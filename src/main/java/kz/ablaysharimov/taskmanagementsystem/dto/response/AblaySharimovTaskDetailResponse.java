package kz.ablaysharimov.taskmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AblaySharimovTaskDetailResponse extends AblaySharimovTaskResponse {

    private List<AblaySharimovCommentResponse> comments;

    private List<AblaySharimovAttachmentResponse> attachments;
}

