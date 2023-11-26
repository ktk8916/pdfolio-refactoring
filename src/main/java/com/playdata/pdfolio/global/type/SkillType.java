package com.playdata.pdfolio.global.type;

import com.playdata.pdfolio.global.exception.BadRequestException;
import com.playdata.pdfolio.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum SkillType {

    JAVA("java"),
    KOTLIN("kotlin"),
    JAVASCRIPT("javascript"),
    TYPESCRIPT("typescript"),
    PYTHON("python"),
    PHP("php"),
    NODE("node"),

    SPRING("spring"),
    NEST("nest"),
    ANGULAR("angular"),
    EXPRESS("express"),
    DJANGO("django"),
    FLASK("flask"),
    LARAVEL("laravel"),
    REACT("react"),
    VUE("vue"),
    NEXT("next"),
    NUXT("nuxt"),
    REACT_NATIVE("react native"),
    MONGODB("mongodb"),

    GRAPHQL("graphql"),
    ORACLE("oracle"),
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),

    AWS("aws"),
    DOCKER("docker"),
    FIREBASE("firebase"),

    GIT("git"),

    ;

    private final String text;

    public static List<SkillType> convertList(List<String> skills) {
        return skills.stream()
                .distinct()
                .map(SkillType::fromName)
                .toList();
    }

    public static SkillType fromName(String skill) {
        return Arrays.stream(values())
                .filter(skillType -> skillType.name().equalsIgnoreCase(skill))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.NO_MATCHING_SKILL));
    }
}
