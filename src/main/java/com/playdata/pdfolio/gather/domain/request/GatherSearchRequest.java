package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.GatherCategory;

import java.util.List;

public record GatherSearchRequest(
        String keyword,
        GatherCategory category,
        List<String> skills
) {
}
