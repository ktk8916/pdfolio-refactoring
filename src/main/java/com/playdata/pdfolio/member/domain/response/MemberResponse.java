package com.playdata.pdfolio.member.domain.response;

import com.playdata.pdfolio.global.type.Skill;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;

import java.util.List;
import java.util.stream.Collectors;

public record MemberResponse(
        Long id,
        String name,
        String providerName,
        String nickName,
        String imageUrl,
        List<String> skills
) {

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getProviderName(),
                member.getNickname(),
                member.getImageUrl(),
                member.getSkills().stream()
                        .map(MemberSkill::getSkill)
                        .map(Skill::getSkillName)
                        .collect(Collectors.toList())
        );
    }
}
