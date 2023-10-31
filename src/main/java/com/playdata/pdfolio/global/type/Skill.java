package com.playdata.pdfolio.global.type;

import com.playdata.pdfolio.global.exception.NoSuchSkillException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Skill {

    // Language
    JAVA(SkillCategory.LANGUAGE, "JAVA"),
    KOTLIN(SkillCategory.LANGUAGE, "KOTLIN"),
    JAVASCRIPT(SkillCategory.LANGUAGE, "JAVASCRIPT"),
    TYPESCRIPT(SkillCategory.LANGUAGE, "TYPESCRIPT"),
    PYTHON(SkillCategory.LANGUAGE, "PYTHON"),
    PHP(SkillCategory.LANGUAGE, "PHP"),

    // Framework
    SPRING(SkillCategory.FRAMEWORK, "SPRING"),
    NEST(SkillCategory.FRAMEWORK, "NEST"),
    EXPRESS(SkillCategory.FRAMEWORK, "EXPRESS"),
    DJANGO(SkillCategory.FRAMEWORK, "DJANGO"),
    LARAVEL(SkillCategory.FRAMEWORK, "LARAVEL"),
    REACT(SkillCategory.FRAMEWORK, "REACT"),
    VUE(SkillCategory.FRAMEWORK, "VUE"),
    NEXT(SkillCategory.FRAMEWORK, "NEXT"),
    NUXT(SkillCategory.FRAMEWORK, "NUXT"),

    // Database
    ORACLE(SkillCategory.DATABASE, "ORACLE"),
    MYSQL(SkillCategory.DATABASE, "MYSQL"),
    POSTGRESQL(SkillCategory.DATABASE, "POSTGRESQL"),

    // Other
    AWS(SkillCategory.ETC, "AWS"),
    DOCKER(SkillCategory.ETC, "DOCKER"),
    GIT(SkillCategory.ETC, "GIT");

    private final SkillCategory category;
    private final String skillName;

    Skill(SkillCategory category, String skillName) {
        this.category = category;
        this.skillName = skillName;
    }

    public static List<Skill> of(List<String> skillNames) {
        return skillNames.stream()
                .map(Skill::findSkillType)
                .collect(Collectors.toList());
    }

    private static Skill findSkillType(String skillName) {
        return Arrays.stream(values())
                .filter(skillType -> skillType.getSkillName().equals(skillName.toUpperCase()))
                .findFirst()
                .orElseThrow(NoSuchSkillException::new);
    }
}
