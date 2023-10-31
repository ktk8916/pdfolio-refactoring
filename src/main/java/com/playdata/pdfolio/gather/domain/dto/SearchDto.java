package com.playdata.pdfolio.gather.domain.dto;

import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SearchDto {
    private String keyword;
    private GatherCategory category;
    private String skills;
}
