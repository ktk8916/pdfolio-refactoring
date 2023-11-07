package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.type.SkillType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GatherSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Gather gather;

    @Enumerated(EnumType.STRING)
    private SkillType skillType;

    public static GatherSkill of(Gather gather, SkillType skillType){
        return GatherSkill.builder()
                .gather(gather)
                .skillType(skillType)
                .build();
    }

    @Builder
    public GatherSkill(Gather gather, SkillType skillType) {
        this.gather = gather;
        this.skillType = skillType;
    }
}
