package com.playdata.pdfolio.gather.domain.dto;

import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.member.domain.dto.MemberDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GatherReplyDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        MemberDto member
) {

    public static GatherReplyDto fromEntity(GatherReply gatherReply){
        return GatherReplyDto.builder()
                .id(gatherReply.getId())
                .content(gatherReply.getContent())
                .createdAt(gatherReply.getCreatedAt())
                .member(MemberDto.fromEntity(gatherReply.getMember()))
                .build();
    }
}
