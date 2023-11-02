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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> save(
            @RequestBody ProjectCreateRequest projectCreateRequest,
            @AuthenticationPrincipal TokenInfo tokenInfo) {
        ProjectCreateResponse response = projectService.save(
                projectCreateRequest,
                tokenInfo.getMemberId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> findById(@PathVariable Long id) {
        ProjectDetailResponse response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ProjectListResponse> search(ProjectSearchParameter searchParameter) {
        ProjectListResponse response = projectService.search(searchParameter);
        return ResponseEntity.ok(response);
    }
}
