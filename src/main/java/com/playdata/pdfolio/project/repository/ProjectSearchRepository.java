package com.playdata.pdfolio.project.repository;

import com.playdata.pdfolio.project.domain.request.ProjectSearchParameter;
import com.playdata.pdfolio.project.domain.response.ProjectResponse;
import org.springframework.data.domain.Page;

public interface ProjectSearchRepository {
    Page<ProjectResponse> findByCondition(ProjectSearchParameter searchParameter);
}
