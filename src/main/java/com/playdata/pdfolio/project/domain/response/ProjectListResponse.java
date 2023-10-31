package com.playdata.pdfolio.project.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectListResponse {

    private List<ProjectResponse> projects;
    private PageInfo pageInfo;


    private ProjectListResponse(List<ProjectResponse> projects, PageInfo pageInfo) {
        this.projects = projects;
        this.pageInfo = pageInfo;
    }

    public static ProjectListResponse of(final Page<ProjectResponse> projects) {
        List<ProjectResponse> content = projects.getContent();
        return new ProjectListResponse(content, PageInfo.of(projects));
    }
}
