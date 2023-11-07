    package com.playdata.pdfolio.gather.domain.response;

    import com.playdata.pdfolio.gather.domain.entity.Gather;
    import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
    import com.playdata.pdfolio.gather.domain.entity.GatherSkill;
    import com.playdata.pdfolio.global.type.SkillType;
    import com.playdata.pdfolio.member.domain.dto.MemberDto;

    import java.time.LocalDate;
    import java.util.List;

    public record GatherDetailResponse(
            Long id,
            String title,
            String content,
            LocalDate startDate,
            LocalDate closeDate,
            int teamSize,
            GatherCategory category,
            String contact,
            int likeCount,
            int viewCount,
            MemberDto member,
            List<SkillType> skills
    ) {
        public static GatherDetailResponse fromEntity(Gather gather){
            return new GatherDetailResponse(
                    gather.getId(),
                    gather.getTitle(),
                    gather.getContent(),
                    gather.getStartDate(),
                    gather.getCloseDate(),
                    gather.getTeamSize(),
                    gather.getCategory(),
                    gather.getContact(),
                    gather.getLikeCount(),
                    gather.getViewCount(),
                    MemberDto.fromEntity(gather.getMember()),
                    gather.getSkills().stream()
                            .map(GatherSkill::getSkillType)
                            .toList()
            );
        }
    }
