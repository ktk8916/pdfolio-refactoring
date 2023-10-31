package com.playdata.pdfolio.gather.domain.response;

import com.playdata.pdfolio.gather.domain.dto.GatherSkillDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
//제목, 기술스택, host 유저, 모집인원, 시작날짜, 마감날짜
public class GatherResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate closeDate;
    private Long teamSize;
    private GatherCategory category;
    private String contact;
    private Integer heartCount;
    private Integer viewCount;
    private Boolean isDeleted;

    private Long memberId;
    private String memberName;
    private String memberImageUrl;

    private List<GatherSkillDto> skills;
    private long commentCount;

    public GatherResponse(Gather gather) {
        this.id = gather.getId();
        this.title = gather.getTitle();
        this.content = gather.getContent();
        this.startDate = gather.getStartDate();
        this.closeDate = gather.getCloseDate();
        this.teamSize = gather.getTeamSize();
        this.commentCount = gather.getComments().stream().filter(comment->!comment.getIsDeleted()).count();
        this.category = gather.getCategory();
        this.contact = gather.getContact();
        this.heartCount = gather.getHeartCount();
        this.viewCount = gather.getViewCount();
        this.memberId = gather.getMember().getId();
        this.memberName = gather.getMember().getNickname();
        this.memberImageUrl = gather.getMember().getImageUrl();
        this.skills = gather.getSkills().stream().map(GatherSkillDto::new).toList();
    }


    public static List<GatherResponse> of(List<Gather> gathers) {
        return gathers.stream()
                .map(GatherResponse::new)
                .toList();
    }
}
