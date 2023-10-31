package com.playdata.pdfolio.project.repository;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.project.domain.request.ProjectSearchParameter;
import com.playdata.pdfolio.project.domain.response.ProjectResponse;
import com.playdata.pdfolio.project.domain.response.ProjectSkillResponse;
import com.playdata.pdfolio.project.domain.response.TempProjectResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.playdata.pdfolio.domain.entity.project.QProject.*;
import static com.playdata.pdfolio.domain.entity.project.QProjectSkill.*;

@RequiredArgsConstructor
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProjectResponse> findByCondition(ProjectSearchParameter searchParameter) {
        queryFactory
                .select(Projections.constructor(TempProjectResponse.class,
                        project.id,
                        project.title,
                        project.description,
                        project.heartCount,
                        project.viewCount,
                        project.thumbNailUrl.url,
                        project.createdAt,
                        Projections.constructor(ProjectSkillResponse.class,
                                projectSkill.skill
                        )
                ))
                .from(project)
                .leftJoin(project.skills, projectSkill)
                .where(project.id.in(
                        JPAExpressions.select(projectSkill.project.id)
                                .from(projectSkill)
                                .where(projectSkillsIn(searchParameter.getSkillCategory().getSkillTypes()))
                ))
                .groupBy(project.id, project.skills)
                .orderBy(project.createdAt.desc())
                .fetch();

        return null;
    }

    private BooleanExpression projectSkillsIn(List<SkillType> skillTypes) {
        if (skillTypes.isEmpty()) {
            return null;
        }

        return projectSkill.skill.in(skillTypes);
    }
}
