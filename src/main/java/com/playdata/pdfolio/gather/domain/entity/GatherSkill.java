package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.type.Skill;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class GatherSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gather_id", nullable = false)
    private Gather gather;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Skill skill;

    public String getSkillName() {
        return this.skill.getSkillName();
    }

}
