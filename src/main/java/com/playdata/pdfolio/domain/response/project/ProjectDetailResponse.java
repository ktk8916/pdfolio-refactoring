package com.playdata.pdfolio.domain.response.project;

import com.playdata.pdfolio.domain.entity.project.Project;
import com.playdata.pdfolio.domain.response.member.MemberInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String content;
    private Integer heartCount;
    private Integer viewCount;
    private Long commentCount;
    private String thumbnailUrl;
    private String createdAt;
    private List<ProjectSkillResponse> skillStacks;
    private MemberInfoResponse author;

    private List<ProjectCommentResponse> comments;

    private ProjectDetailResponse(final Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.content = project.getContent();
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
        this.comments = project.getComments()
                .stream()
                .map(ProjectCommentResponse::of)
                .toList();
    }

    public static ProjectDetailResponse of(final Project project) {
        return new ProjectDetailResponse(project);
    }

}
