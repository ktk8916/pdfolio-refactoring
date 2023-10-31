package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.member.domain.entity.Member;

public record WriteReplyRequest(
        Long commentId,
        String content
) {
    public GatherReply toEntity(Long memberId){

        return GatherReply.builder()
                .member(Member.builder().id(memberId).build())
                .comment(GatherComment.builder().id(commentId).build())
                .content(content)
                .build();
    }
}
