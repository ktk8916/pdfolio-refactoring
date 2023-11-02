package com.playdata.pdfolio.member.domain.response;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import com.playdata.pdfolio.member.domain.entity.MemberStatus;

import java.util.List;

public record MemberDetailResponse(
        Long id,
        String provider,
        String nickname,
        String imageUrl,
        MemberStatus status,
        List<SkillType> skills
) {

    public static MemberDetailResponse fromEntity(Member member){
        return new MemberDetailResponse(
                member.getId(),
                member.getProvider(),
                member.getNickname(),
                member.getImageUrl(),
                member.getStatus(),
                member.getSkills().stream()
                        .map(MemberSkill::getSkillType)
                        .toList()
        );
    }
}
