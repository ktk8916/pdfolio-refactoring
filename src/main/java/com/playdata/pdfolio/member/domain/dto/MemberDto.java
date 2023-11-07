package com.playdata.pdfolio.member.domain.dto;

import com.playdata.pdfolio.member.domain.entity.Member;

public record MemberDto(
        Long id,
        String nickname,
        String imageUrl
) {
    public static MemberDto fromEntity(Member member){
        return new MemberDto(
                member.getId(),
                member.getNickname(),
                member.getImageUrl()
        );
    }
}
