package com.playdata.pdfolio.project.domain.response;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.project.domain.entity.ProjectSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ProjectSkillResponse {

    private String skillName;

    public ProjectSkillResponse(SkillType skillType) {
        this.skillName = skillType.getText();
    }

    public static ProjectSkillResponse of(ProjectSkill projectSkill) {
        return new ProjectSkillResponse(projectSkill.getSkillType());
    }

    public static List<ProjectSkillResponse> of(List<ProjectSkill> projectSkills) {
        return projectSkills.stream()
                .map(ProjectSkillResponse::of)
                .toList();
    }
}
