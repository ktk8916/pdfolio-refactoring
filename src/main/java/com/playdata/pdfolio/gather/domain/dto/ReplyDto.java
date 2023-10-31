package com.playdata.pdfolio.gather.domain.dto;

import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyDto {
    private Long id;
    private Long memberId;
    private String nickName;
    private String content;
    private boolean isDeleted;
    public ReplyDto(GatherReply gatherReply){
        this.id = gatherReply.getId();
        this.memberId = gatherReply.getMember().getId();
        this.nickName = gatherReply.getMember().getNickname();
        this.content = gatherReply.getContent();
        this.isDeleted = gatherReply.getIsDeleted();
    }
}
