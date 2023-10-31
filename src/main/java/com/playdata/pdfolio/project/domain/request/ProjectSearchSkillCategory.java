package com.playdata.pdfolio.project.domain.request;

import com.playdata.pdfolio.global.type.SkillType;
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

    private List<SkillType> skillTypes;

    public static ProjectSearchSkillCategory of(String skillCategory) {
        if (skillCategory == null || skillCategory.isBlank()) {
            return new ProjectSearchSkillCategory(List.of(SkillType.values()));
        }

        List<SkillType> skillTypes = Arrays.stream(skillCategory.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(SkillType::valueOf)
                .toList();

        return new ProjectSearchSkillCategory(skillTypes);
    }

}
