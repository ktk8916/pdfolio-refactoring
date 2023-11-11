package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.member.domain.entity.Member;

public record GatherCommentWriteRequest(
        String content
) {
    public GatherComment toEntity(Long memberId, Long gatherId){
        return GatherComment.builder()
                .member(Member.fromId(memberId))
                .gather(Gather.formId(gatherId))
                .content(content)
                .build();
    }
}
