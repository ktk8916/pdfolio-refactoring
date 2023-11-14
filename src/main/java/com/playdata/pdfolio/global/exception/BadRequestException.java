package com.playdata.pdfolio.global.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(ErrorCode e) {
        super(e.name());
    }
}
