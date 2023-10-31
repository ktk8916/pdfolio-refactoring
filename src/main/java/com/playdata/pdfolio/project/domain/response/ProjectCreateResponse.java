package com.playdata.pdfolio.project.domain.response;

import com.playdata.pdfolio.project.domain.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectCreateResponse {

    private Long projectId;
    private String title;
    private String description;
    private List<ProjectSkillResponse> projectSkills;

    public ProjectCreateResponse(
            final Long projectId,
            final String title,
            final String description,
            final List<ProjectSkillResponse> projectSkills) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.projectSkills = projectSkills;
    }

    public static ProjectCreateResponse of(final Project project) {
        List<ProjectSkillResponse> projectSkills = ProjectSkillResponse.of(project.getSkills());

        return new ProjectCreateResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                projectSkills
        );
    }
}
