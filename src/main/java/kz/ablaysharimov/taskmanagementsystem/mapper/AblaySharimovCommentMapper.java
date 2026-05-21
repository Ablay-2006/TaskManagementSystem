package kz.ablaysharimov.taskmanagementsystem.mapper;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovCommentResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AblaySharimovCommentMapper {

    public AblaySharimovCommentResponse toResponse(AblaySharimovComment comment) {
        if (comment == null) {
            return null;
        }
        return AblaySharimovCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorUsername(comment.getAuthor() != null ? comment.getAuthor().getUsername() : null)
                .build();
    }

    public List<AblaySharimovCommentResponse> toResponseList(List<AblaySharimovComment> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

