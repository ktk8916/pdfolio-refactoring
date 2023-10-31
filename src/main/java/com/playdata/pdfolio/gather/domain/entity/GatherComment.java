package com.playdata.pdfolio.gather.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import com.playdata.pdfolio.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class GatherComment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private Gather gather;
    @ManyToOne
    private Member member;
    @OneToMany(mappedBy = "comment")
    private List<GatherReply> replies;
}
