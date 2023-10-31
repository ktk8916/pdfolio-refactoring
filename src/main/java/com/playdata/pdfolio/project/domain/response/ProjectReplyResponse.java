package com.playdata.pdfolio.project.domain.response;

import com.playdata.pdfolio.project.domain.entity.ProjectReply;
import com.playdata.pdfolio.member.domain.response.MemberInfoResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectReplyResponse {

    private Long replyId;
    private String content;
    private String createdAt;
    private MemberInfoResponse author;

    public static ProjectReplyResponse of(final ProjectReply projectReply) {
        return new ProjectReplyResponse(
                projectReply.getId(),
                projectReply.getContent(),
                projectReply.getCreatedAt().toString(),
                MemberInfoResponse.of(projectReply.getMember())
        );
    }
}
