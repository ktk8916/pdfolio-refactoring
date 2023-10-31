package com.playdata.pdfolio.project.domain.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProjectCreateRequest {

    private String title;

    private String content;

    private String description;
    private String repositoryUrl;

    private String publishUrl;

    private String thumbNailUrl;

    private List<String> projectSkills;
}
