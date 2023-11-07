package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GatherComment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Gather gather;
    @ManyToOne
    private Member member;
    private String content;
    @OneToMany(mappedBy = "comment")
    private List<GatherReply> replies;

    public static GatherComment fromId(Long id){
        GatherComment gatherComment = new GatherComment();
        gatherComment.id = id;
        return gatherComment;
    }

    @Builder
    public GatherComment(Gather gather, Member member, String content, List<GatherReply> replies) {
        this.gather = gather;
        this.member = member;
        this.content = content;
        this.replies = replies;
    }
}
