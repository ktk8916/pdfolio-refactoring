package com.playdata.pdfolio.global.type;

import com.playdata.pdfolio.global.exception.NoSuchSkillException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum SkillType {

    // Language
    JAVA("java"),
    KOTLIN("kotlin"),
    JAVASCRIPT("javascript"),
    TYPESCRIPT("typescript"),
    PYTHON("python"),
    PHP("php"),

    // Framework
    SPRING("spring"),
    NEST("nest"),
    EXPRESS("express"),
    DJANGO("django"),
    LARAVEL("laravel"),
    REACT("react"),
    VUE("vue"),
    NEXT("next"),
    NUXT("nuxt"),

    // database
    ORACLE("oracle"),
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),

    // infra
    AWS("aws"),
    DOCKER("docker"),

    // etc
    GIT("git"),

    ;

    private final String name;

    public static List<SkillType> of(List<String> skillNames) {
        return skillNames.stream()
                .map(SkillType::findSkillType)
                .collect(Collectors.toList());
    }

    private static SkillType findSkillType(String skillName) {
        return Arrays.stream(values())
                .filter(skillType -> skillType.getName().equals(skillName.toUpperCase()))
                .findFirst()
                .orElseThrow(NoSuchSkillException::new);
    }
}
