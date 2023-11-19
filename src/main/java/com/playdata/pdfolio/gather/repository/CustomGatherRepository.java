package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CustomGatherRepository {
    Page<GatherResponse> findAllByCondition(
            PageRequest request,
            SearchDto searchDto
    );

    List<GatherComment> findCommentsByGatherId(Long gatherId);

}
