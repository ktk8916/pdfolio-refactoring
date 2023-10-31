package com.playdata.pdfolio.project.domain.request;

import com.playdata.pdfolio.global.type.Skill;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectSearchSkillCategory {

    private List<Skill> skills;

    public static ProjectSearchSkillCategory of(String skillCategory) {
        if (skillCategory == null || skillCategory.isBlank()) {
            return new ProjectSearchSkillCategory(List.of(Skill.values()));
        }

        List<Skill> skills = Arrays.stream(skillCategory.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(Skill::valueOf)
                .toList();

        return new ProjectSearchSkillCategory(skills);
    }

}
