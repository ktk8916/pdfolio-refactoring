package com.playdata.pdfolio.member.domain.response;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;

import java.util.List;
import java.util.stream.Collectors;

public record MemberResponse(
        Long id,
        String providerName,
        String nickname,
        String imageUrl,
        List<String> skills
) {

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getId(),
                member.getProvider(),
                member.getNickname(),
                member.getImageUrl(),
                member.getSkills().stream()
                        .map(MemberSkill::getSkillType)
                        .map(SkillType::getName)
                        .collect(Collectors.toList())
        );
    }
}
