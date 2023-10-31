package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class Gather extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    private LocalDate startDate;
    private LocalDate closeDate;

    private Long teamSize;

    @Enumerated(EnumType.STRING)
    private GatherCategory category;

    private String contact;

    @Builder.Default
    private Integer heartCount = 0;

    @Builder.Default
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "gather")
    private Set<GatherSkill> skills;

    @OneToMany(mappedBy = "gather", fetch = FetchType.LAZY)
    private List<GatherComment> comments;

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void increaseHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        this.heartCount--;
    }

}
