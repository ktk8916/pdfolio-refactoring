package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.global.type.SkillType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.playdata.pdfolio.gather.domain.entity.QGather.gather;
import static com.playdata.pdfolio.gather.domain.entity.QGatherComment.gatherComment;
import static com.playdata.pdfolio.gather.domain.entity.QGatherReply.gatherReply;
import static com.playdata.pdfolio.gather.domain.entity.QGatherSkill.gatherSkill;
import static com.playdata.pdfolio.member.domain.entity.QMember.member;


public class CustomGatherRepositoryImpl implements CustomGatherRepository {
    private final JPAQueryFactory queryFactory;
    public CustomGatherRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Gather> findGathersByCondition(String keyword, GatherCategory category, List<SkillType> skills, Pageable pageRequest) {

        List<Gather> gathers = queryFactory.selectFrom(gather)
                .innerJoin(gather.member, member)
                .fetchJoin()
                .leftJoin(gather.skills, gatherSkill)
                .where(
                        matchTitleAndContentAgainstKeyword(keyword),
                        eqCategory(category),
                        inSkills(skills)
                )
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset())
                .fetch();

        Long totalSize = queryFactory.select(gather.count())
                .from(gather)
                .leftJoin(gather.skills, gatherSkill)
                .where(
                        matchTitleAndContentAgainstKeyword(keyword),
                        eqCategory(category),
                        inSkills(skills)
                )
                .fetchOne();

        return new PageImpl<>(gathers, pageRequest, totalSize);
    }

    private BooleanExpression matchTitleAndContentAgainstKeyword(String keyword){
        return keyword == null ? null : Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1}, {2})",
                gather.title,
                gather.content,
                keyword).gt(0);
    }

    private BooleanExpression inSkills(List<SkillType> skills) {
        return skills == null ? null : gatherSkill.skillType.in(skills);
    }

    private BooleanExpression eqCategory(GatherCategory category) {
        return category == null ? null : gather.category.eq(category);
    }

    private BooleanExpression containsKeywordTitleOrContent(String keyword) {
        return keyword == null ? null : gather.title.contains(keyword).or(gather.content.contains(keyword));
    }

    @Override
    public List<GatherComment> findCommentsByGatherId(Long gatherId) {
        return queryFactory.selectFrom(gatherComment)
                .leftJoin(gatherComment.replies, gatherReply)
                .fetchJoin()
                .where(
                        gatherComment.gather.id.eq(gatherId),
                        gatherComment.isDeleted.isFalse(),
                        gatherReply.isNull().or(gatherReply.isDeleted.isFalse())
                )
                .fetch();
    }
}
