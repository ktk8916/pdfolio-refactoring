package com.playdata.pdfolio.gather.domain.dto;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.member.domain.dto.MemberDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GatherThumbnailDto(
        Long id,
        String title,
        String content,
        int likeCount,
        int viewCount,
        LocalDateTime createdAt,
        MemberDto member
) {
    public static GatherThumbnailDto fromEntity(Gather gather){
        return GatherThumbnailDto.builder()
                .id(gather.getId())
                .title(gather.getTitle())
                .content(gather.getContent())
                .likeCount(gather.getLikeCount())
                .viewCount(gather.getViewCount())
                .createdAt(gather.getCreatedAt())
                .member(MemberDto.fromEntity(gather.getMember()))
                .build();
    }
}
