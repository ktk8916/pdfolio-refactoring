package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.member.domain.entity.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GatherEditRequest(
        String title,
        String content,
        LocalDate startDate,
        LocalDate closeDate,
        int teamSize,
        GatherCategory category,
        String contact,
        List<String> skills
) {
}