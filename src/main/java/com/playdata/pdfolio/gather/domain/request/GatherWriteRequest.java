package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.member.domain.entity.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record GatherWriteRequest(
        String title,
        String content,
        LocalDate startDate,
        LocalDate closeDate,
        int teamSize,
        GatherCategory category,
        String contact,
        List<String> skills
) {
    public Gather toEntity(Long memberId){
        return Gather.builder()
                .member(Member.fromId(memberId))
                .title(title)
                .content(content)
                .teamSize(teamSize)
                .startDate(startDate)
                .closeDate(closeDate)
                .category(category)
                .skills(new ArrayList<>())
                .contact(contact)
                .build();
    }
}