package com.playdata.pdfolio.global.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private String contentType;
    private Long contentId;
    private Long memberId;
    public ForbiddenException(ErrorCode e) {
        super(e.name());
    }
    public ForbiddenException(ErrorCode e, Class<?> content, Long contentId, Long memberId) {
        super(e.name());
        this.contentType = content.getSimpleName();
        this.contentId = contentId;
        this.memberId = memberId;
    }

    public String getLog(){
        return String.format("contentType : %s contentId : %d memberId : %d", contentType, contentId, memberId);
    }
}
