package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.global.type.SkillType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomGatherRepository {
    List<Gather> findGathersByCondition(String keyword, GatherCategory category, List<SkillType> skills, Pageable pageRequest);

    List<GatherComment> findCommentsByGatherId(Long gatherId);

}
