package com.playdata.pdfolio.project.domain.request;

import com.playdata.pdfolio.project.exception.InValidProjectSearchTypeException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum ProjectSearchSortType {

    CREATED_AT("createdAt"),
    VIEW_COUNT("viewCount"),
    HEART_COUNT("heartCount")
    ;

    private String type;

    ProjectSearchSortType(String type) {
        this.type = type;
    }

    public static ProjectSearchSortType of(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return CREATED_AT;
        }

        return Arrays.stream(values())
                .filter(type -> type.type.equals(value))
                .findFirst()
                .orElseThrow(InValidProjectSearchTypeException::new);
    }

}
