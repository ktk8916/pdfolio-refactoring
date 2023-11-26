package com.playdata.pdfolio.gather.domain.response;

import com.playdata.pdfolio.gather.domain.dto.GatherThumbnailDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import lombok.Builder;

import java.util.List;

@Builder
public record GatherSearchResponse(
        List<GatherThumbnailDto> gathers,
        int size
) {
    public static GatherSearchResponse of(List<Gather> gathers, int size){
        return GatherSearchResponse.builder()
                .gathers(gathers.stream()
                        .map(GatherThumbnailDto::fromEntity)
                        .toList())
                .size(size)
                .build();
    }
}
