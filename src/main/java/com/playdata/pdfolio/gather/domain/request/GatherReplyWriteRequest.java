package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.member.domain.entity.Member;

public record GatherReplyWriteRequest(
        String content
) {
    public GatherReply toEntity(Long commentId, Long memberId){
        return GatherReply.builder()
                .comment(GatherComment.fromId(commentId))
                .member(Member.fromId(memberId))
                .content(content)
                .build();
    }
}
