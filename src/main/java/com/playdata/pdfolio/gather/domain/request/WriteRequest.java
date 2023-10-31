package com.playdata.pdfolio.gather.domain.request;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.List;

public record WriteRequest(
        String title,
        String content,
        LocalDate startDate,
        LocalDate closeDate,
        Long teamSize,
        @Enumerated(EnumType.STRING)
        GatherCategory category,
        String contact,
        List<String> skills
) {
    public Gather toEntity(Long memberId){
        return Gather.builder()
                .member(Member.builder().id(memberId).build())
                .title(title)
                .content(content)
                .teamSize(teamSize)
                .startDate(startDate)
                .closeDate(closeDate)
                .category(category)
                .contact(contact)
                .build();
    }
}