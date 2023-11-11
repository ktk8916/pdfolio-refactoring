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
    private GatherComment comment;
    @ManyToOne
    private Member member;
    private String content;

    public static GatherReply createGatherReply(GatherComment gatherComment, Member member, String content){
        return GatherReply.builder()
                .comment(gatherComment)
                .member(member)
                .content(content)
                .build();
    }

    @Builder
    public GatherReply(GatherComment comment, Member member, String content) {
        this.comment = comment;
        this.member = member;
        this.content = content;
    }
}
