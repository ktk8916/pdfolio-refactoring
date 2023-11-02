package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.type.SkillType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSkill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @Enumerated(EnumType.STRING)
    private SkillType skillType;

    public static MemberSkill of(Member member, SkillType skillType){
        return MemberSkill.builder()
                .member(member)
                .skillType(skillType)
                .build();
    }

    @Builder
    public MemberSkill(Member member, SkillType skillType) {
        this.member = member;
        this.skillType = skillType;
    }
}
