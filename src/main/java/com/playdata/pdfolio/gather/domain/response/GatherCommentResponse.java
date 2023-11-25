package com.playdata.pdfolio.gather.domain.response;

import com.playdata.pdfolio.gather.domain.dto.GatherReplyDto;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.member.domain.dto.MemberDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GatherCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        MemberDto member,
        List<GatherReplyDto> replies
) {
    public static GatherCommentResponse fromEntity(GatherComment gatherComment){
        return GatherCommentResponse.builder()
                .id(gatherComment.getId())
                .content(gatherComment.getContent())
                .createdAt(gatherComment.getCreatedAt())
                .member(MemberDto.fromEntity(gatherComment.getMember()))
                .replies(gatherComment.getReplies().stream()
                        .map(GatherReplyDto::fromEntity)
                        .toList())
                .build();
    }
}
