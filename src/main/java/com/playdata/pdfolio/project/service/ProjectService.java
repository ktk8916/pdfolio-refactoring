package com.playdata.pdfolio.project.service;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.project.domain.entity.Project;
import com.playdata.pdfolio.project.domain.entity.ProjectSkill;
import com.playdata.pdfolio.project.domain.entity.Url;
import com.playdata.pdfolio.project.domain.request.ProjectCreateRequest;
import com.playdata.pdfolio.project.domain.request.ProjectSearchParameter;
import com.playdata.pdfolio.project.domain.response.ProjectCreateResponse;
import com.playdata.pdfolio.project.domain.response.ProjectDetailResponse;
import com.playdata.pdfolio.project.domain.response.ProjectListResponse;
import com.playdata.pdfolio.project.domain.response.ProjectResponse;
import com.playdata.pdfolio.project.exception.ProjectNotFoundException;
import com.playdata.pdfolio.project.repository.ProjectRepository;
import com.playdata.pdfolio.project.repository.ProjectSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;

    @Transactional
    public ProjectCreateResponse save(final ProjectCreateRequest request, final Long memberId) {
        Member member = Member.fromId(memberId);

        Project project = createProject(request, member);
        List<SkillType> skillTypes = SkillType.convertList(request.getProjectSkills());

        Project savedProject = projectRepository.save(project);

        List<ProjectSkill> projectSkills = createProjectSkills(savedProject, skillTypes);
        projectSkillRepository.saveAll(projectSkills);

        return ProjectCreateResponse.of(savedProject);
    }

    private List<ProjectSkill> createProjectSkills(final Project project, final List<SkillType> skillTypes) {
        return skillTypes.stream()
                .map(skill -> ProjectSkill.builder()
                                .project(project)
                                .skillType(skill)
                                .build())
                .toList();
    }

    private Project createProject(final ProjectCreateRequest request, final Member member) {
        return Project.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .description(request.getDescription())
                .repositoryUrl(Url.of(request.getRepositoryUrl()))
                .publishUrl(Url.of(request.getPublishUrl()))
                .thumbNailUrl(Url.of(request.getThumbNailUrl()))
                .member(member)
                .build();
    }

    @Transactional
    public ProjectDetailResponse findById(final Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(ProjectNotFoundException::new);

        project.increaseViewCount();

        return ProjectDetailResponse.of(project);
    }

    public ProjectListResponse search(final ProjectSearchParameter searchParameter) {
        String sortType = searchParameter.getSortType().getType();
        Page<Project> projects = null;
        if (sortType.equals("createdAt")) {
            projects = projectRepository.searchByConditionOrderByCreatedAt(
                    searchParameter.getSkillCategory().getSkillTypes(),
                    searchParameter.getPageable()
            );
        } else if (sortType.equals("viewCount")) {
            projects = projectRepository.searchByConditionOrderByViewCount(
                    searchParameter.getSkillCategory().getSkillTypes(),
                    searchParameter.getPageable()
            );
        } else if (sortType.equals("heartCount")) {
            projects = projectRepository.searchByConditionOrderByHeartCount(
                    searchParameter.getSkillCategory().getSkillTypes(),
                    searchParameter.getPageable()
            );
        }

        Page<ProjectResponse> projectResponses = projects.map(ProjectResponse::new);

        return ProjectListResponse.of(projectResponses);
    }
}
