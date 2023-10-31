package com.playdata.pdfolio.project.repository;

import com.playdata.pdfolio.RepositoryTest;
import com.playdata.pdfolio.project.domain.request.ProjectSearchParameter;
import org.junit.jupiter.api.Test;

class ProjectRepositoryTest extends RepositoryTest {

    @Test
    void findByCondition() {
        ProjectSearchParameter parameter = ProjectSearchParameter.of("1", "10", "createdAt", "JAVA,SPRING");
        projectRepository.findByCondition(parameter);
    }

}