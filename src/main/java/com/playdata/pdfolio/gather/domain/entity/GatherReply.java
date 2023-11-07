package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GatherReply extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private String content;
    @ManyToOne
    private GatherComment comment;

    @Builder
    public GatherReply(Member member, String content, GatherComment comment) {
        this.member = member;
        this.content = content;
        this.comment = comment;
    }
}
