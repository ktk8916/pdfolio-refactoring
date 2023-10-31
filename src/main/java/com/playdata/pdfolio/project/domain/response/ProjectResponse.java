package com.playdata.pdfolio.project.domain.response;

import com.playdata.pdfolio.project.domain.entity.Project;
import com.playdata.pdfolio.member.domain.response.MemberInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private Integer heartCount;
    private Integer viewCount;
    private Long commentCount;
    private String thumbnailUrl;
    private String createdAt;
    private List<ProjectSkillResponse> skillStacks;
    private MemberInfoResponse author;

    public ProjectResponse(final Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.heartCount = project.getHeartCount();
        this.viewCount = project.getViewCount();
        this.commentCount = project.getCommentCount();
        this.thumbnailUrl = project.getThumbNailUrl().getUrl();
        this.createdAt = project.getCreatedAt().toString();
        this.skillStacks = project.getSkills()
                .stream()
                .map(ProjectSkillResponse::of)
                .toList();
        this.author = MemberInfoResponse.of(project.getMember());
    }

    public static List<ProjectResponse> of(final Page<Project> projects) {
        return projects.stream()
                .map(ProjectResponse::new)
                .toList();
    }
}
