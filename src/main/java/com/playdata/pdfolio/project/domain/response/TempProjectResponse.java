package com.playdata.pdfolio.project.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class TempProjectResponse {

    private Long id;
    private String title;
    private String description;
    private Integer heartCount;
    private Integer viewCount;
    private String thumbnailUrl;
    private String createdAt;
    private List<ProjectSkillResponse> skillStacks;

    public TempProjectResponse(final Long id,
                               final String title,
                               final String description,
                               final Integer heartCount,
                               final Integer viewCount,
                               final String thumbnailUrl,
                               final LocalDateTime createdAt,
                               final List<ProjectSkillResponse> skillStacks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.heartCount = heartCount;
        this.viewCount = viewCount;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt.toString();
        this.skillStacks = skillStacks;
    }
}
