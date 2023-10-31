package com.playdata.pdfolio.gather.domain.dto;

import com.playdata.pdfolio.gather.domain.entity.GatherSkill;

public class GatherSkillDto{
    private Long id;
    private String skill;

    public Long getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }

    public GatherSkillDto(GatherSkill gatherSkill) {
        this.id = gatherSkill.getId();
        this.skill = gatherSkill.getSkillName();
    }
}
