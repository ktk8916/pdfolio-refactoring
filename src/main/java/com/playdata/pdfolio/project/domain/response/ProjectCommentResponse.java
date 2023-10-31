package com.playdata.pdfolio.project.domain.response;

import com.playdata.pdfolio.project.domain.entity.ProjectComment;
import com.playdata.pdfolio.member.domain.response.MemberInfoResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectCommentResponse {

    private Long commentId;
    private String content;
    private String createdAt;
    private MemberInfoResponse author;
    List<ProjectReplyResponse> replies;

    public static ProjectCommentResponse of(final ProjectComment projectComment) {
        return new ProjectCommentResponse(
                projectComment.getId(),
                projectComment.getContent(),
                projectComment.getCreatedAt().toString(),
                MemberInfoResponse.of(projectComment.getMember()),
                projectComment.getReplies()
                        .stream()
                        .map(ProjectReplyResponse::of)
                        .toList()
        );
    }
}
