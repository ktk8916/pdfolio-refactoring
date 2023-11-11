package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.member.domain.entity.Member;

public record GatherCommentWriteRequest(
        String content
) {
    public GatherComment toEntity(Long gatherId, Long memberId){
        return GatherComment.builder()
                .gather(Gather.formId(gatherId))
                .member(Member.fromId(memberId))
                .content(content)
                .build();
    }
}
