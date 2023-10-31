package com.playdata.pdfolio.global.type;

import lombok.Getter;

@Getter
public enum SkillCategory {

    LANGUAGE("LANGUAGE"),
    FRAMEWORK("FRAMEWORK"),
    DATABASE("DATABASE"),
    ETC("ETC"),
    ;

    private final String skillCategoryName;

    SkillCategory(String skillCategoryName) {
        this.skillCategoryName = skillCategoryName;
    }
}
