package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GatherSearchRepository {
    Page<GatherResponse> findAllByCondition(
            PageRequest request,
            SearchDto searchDto
    );

    Gather findByIdIncludingUndeletedComments(Long id);

}
