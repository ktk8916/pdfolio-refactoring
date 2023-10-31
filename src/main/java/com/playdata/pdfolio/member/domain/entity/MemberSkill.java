package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.type.SkillType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MemberSkill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @Enumerated(EnumType.STRING)
    private SkillType skillType;
}
