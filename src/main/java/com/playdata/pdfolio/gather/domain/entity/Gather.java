package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Gather extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "longtext")
    private String content;
    private LocalDate startDate;
    private LocalDate closeDate;
    private int teamSize;
    @Enumerated(EnumType.STRING)
    private GatherCategory category;
    private String contact;
    private int likeCount;
    private int viewCount;
    @ManyToOne
    private Member member;
    @BatchSize(size = 50)
    @OneToMany(mappedBy = "gather", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GatherSkill> skills = new ArrayList<>();
    @OneToMany(mappedBy = "gather")
    private List<GatherComment> comments = new ArrayList<>();

    public static Gather formId(Long id){
        Gather gather = new Gather();
        gather.id = id;
        return gather;
    }

    public void replaceGatherSkills(List<SkillType> skillTypes){
        this.skills.clear();
        List<GatherSkill> gatherSkills = skillTypes.stream()
                .map(skillType -> GatherSkill.of(this, skillType))
                .toList();
        skills.addAll(gatherSkills);
    }

    public void edit(String title, String content, LocalDate startDate, LocalDate closeDate, int teamSize, GatherCategory category, String contact, List<SkillType> skillTypes){
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.teamSize = teamSize;
        this.category = category;
        this.contact = contact;
        replaceGatherSkills(skillTypes);
    }

    @Builder
    public Gather(String title, String content, LocalDate startDate, LocalDate closeDate, int teamSize, GatherCategory category, String contact, int likeCount, int viewCount, Member member, List<GatherSkill> skills, List<GatherComment> comments) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.teamSize = teamSize;
        this.category = category;
        this.contact = contact;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.member = member;
        this.skills = skills;
        this.comments = comments;
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void increaseHeartCount() {
        this.likeCount++;
    }

    public void decreaseHeartCount() {
        this.likeCount--;
    }
}
