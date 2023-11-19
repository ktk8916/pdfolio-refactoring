package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.QGatherSkill;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import com.playdata.pdfolio.global.type.SkillType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static com.playdata.pdfolio.gather.domain.entity.QGather.gather;
import static com.playdata.pdfolio.gather.domain.entity.QGatherComment.gatherComment;
import static com.playdata.pdfolio.gather.domain.entity.QGatherReply.gatherReply;
import static com.playdata.pdfolio.gather.domain.entity.QGatherSkill.gatherSkill;
import static com.playdata.pdfolio.member.domain.entity.QMember.member;


public class CustomGatherRepositoryImpl implements CustomGatherRepository {
    private final JPAQueryFactory queryFactory;

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

    @Override
    public Page<GatherResponse> findAllByCondition(
            PageRequest request,
            SearchDto condition
    ){

        QGatherSkill qGatherSkill = gatherSkill;

        List<Long> gatherIdsWithSkills = queryFactory
                .select(qGatherSkill.gather.id)
                .from(qGatherSkill)
                .where(skillEqualString(condition.getSkills()))
                .fetch();

        BooleanExpression isDeletedExpression = gather.isDeleted.eq(false);
        JPAQuery<Gather> query = queryFactory
                .select(gather)
                .from(gather)
                .leftJoin(gather.member, member)
                .leftJoin(gather.skills, gatherSkill)
                .fetchJoin()
                .where(
                        isDeletedExpression
                        ,(categoryEqual(condition.getCategory()))
                        ,(keywordContains(condition.getKeyword()))
//                        ,skillEqual(gatherSkill.skill,condition.getSkills())
                        ,skillEqual2(condition.getSkills())
                )
                .offset(request.getPageNumber())
                .limit(request.getPageSize());

        List<Gather> gathers = query.fetch();
        List<GatherResponse> gatherResponses = GatherResponse.of(gathers);

        Long totalSize = queryFactory
                .select(gather.count())
                .from(gather)
                .where(
                        isDeletedExpression
                        ,(categoryEqual(condition.getCategory()))
                        ,(keywordContains(condition.getKeyword()))
                        ,skillEqual(gatherIdsWithSkills,condition.getSkills())
                )
                .fetchOne();

        return new PageImpl<>(gatherResponses, request, totalSize);
    }


    private BooleanExpression keywordContains(String keyword) {
        return keyword == null
                ? null
                : gather.content.contains(keyword).or(gather.title.contains(keyword));
    }

    private BooleanExpression categoryEqual(GatherCategory category) {
        return category == null
                ? null
                : gather.category.eq(category);
    }

    private BooleanExpression skillEqual(List<Long> gatherIdsWithSkills, String getSkills) {
        return getSkills == null || gatherIdsWithSkills.isEmpty()
                ? null
                : gather.id.in(gatherIdsWithSkills);
    }
    private BooleanExpression skillEqual2(String getSkills) {
        return getSkills == null || getSkills.isEmpty()
                ? null
                : gatherSkill.skillType.in(SkillType.convertList(Arrays.asList(getSkills.split(","))));
    }
    private BooleanBuilder skillEqualString(String skills) {
        BooleanBuilder builder = new BooleanBuilder();
        QGatherSkill qGatherSkill = gatherSkill;
        if(skills==null || skills.isEmpty() ) return  builder;
        String[] split = skills.split(",");
        for (String c : split) {
            builder.or(qGatherSkill.skillType.in(SkillType.valueOf(c)));
        }
        return builder;
    }




    public CustomGatherRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
