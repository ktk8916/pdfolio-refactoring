package com.playdata.pdfolio.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomExceptionTest {

    @DisplayName("ForbiddenException의 예외내용을 로그로 반환한다.")
    @Test
    void getLogForbiddenException(){
        // given
        ForbiddenException exception = new ForbiddenException(ErrorCode.INVALID_AUTHOR, Object.class, 11L, 22L);

        // when
        String log = exception.getLog();

        // then
        assertThat(log).isEqualTo("contentType : Object contentId : 11 memberId : 22");
    }

    @DisplayName("NotFoundExceptiond의 예외내용을 로그로 반환한다.")
    @Test
    void getLogNotFoundExceptiond(){
        // given
        NotFoundException exception = new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, Object.class, 11L);

        // when
        String log = exception.getLog();

        // then
        assertThat(log).isEqualTo("contentType : Object contentId : 11");
    }
}