package com.playdata.pdfolio.project.controller;

import com.playdata.pdfolio.jwt.TokenInfo;
import com.playdata.pdfolio.project.domain.request.ProjectCreateRequest;
import com.playdata.pdfolio.project.domain.request.ProjectSearchParameter;
import com.playdata.pdfolio.project.domain.response.ProjectCreateResponse;
import com.playdata.pdfolio.project.domain.response.ProjectDetailResponse;
import com.playdata.pdfolio.project.domain.response.ProjectListResponse;
import com.playdata.pdfolio.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectCreateResponse save(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody ProjectCreateRequest projectCreateRequest
    ) {
        return projectService.save(projectCreateRequest, tokenInfo.getId());
    }

    @GetMapping("/{id}")
    public ProjectDetailResponse findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @GetMapping("/search")
    public ProjectListResponse search(ProjectSearchParameter searchParameter) {
        return projectService.search(searchParameter);
    }
}
