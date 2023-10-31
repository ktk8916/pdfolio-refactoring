package com.playdata.pdfolio.project.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "longtext")
    private String content;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "repository_url", nullable = false))
    })
    private Url repositoryUrl;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "publish_url"))
    })
    private Url publishUrl;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "thumbnail_url", nullable = false))
    })
    private Url thumbNailUrl;

    @Column(name = "heart_count")
    @Builder.Default
    private Integer heartCount = 0;

    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<ProjectComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<ProjectSkill> skills = new ArrayList<>();

    public Long getCommentCount() {
        return (long) this.comments.size();
    }

    public void increaseHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        this.heartCount--;
    }

    public void increaseViewCount() {
        this.heartCount++;
    }

    public void decreaseViewCount() {
        this.heartCount--;
    }
}
