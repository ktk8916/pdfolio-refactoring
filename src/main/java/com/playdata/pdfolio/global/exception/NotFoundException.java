package com.playdata.pdfolio.global.exception;

public class NotFoundException extends RuntimeException {
    private final String contentType;
    private final Long contentId;
    public NotFoundException(ErrorCode e, Class<?> content, Long contentId) {
        super(e.name());
        this.contentType = content.getSimpleName();
        this.contentId = contentId;
    }

    public String getLog(){
        return String.format("contentType : %s contentId : %d", contentType, contentId);
    }
}
