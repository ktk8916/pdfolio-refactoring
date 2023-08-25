package com.playdata.pdfolio.global.exception;

import com.playdata.pdfolio.project.exception.*;
import com.playdata.pdfolio.auth.exception.AccessTokenExpiredException;
import com.playdata.pdfolio.auth.exception.InvalidTokenException;
import com.playdata.pdfolio.auth.exception.TokenNotFoundException;
import com.playdata.pdfolio.member.exception.MemberNotFoundException;
import com.playdata.pdfolio.member.exception.UnregisteredMemberException;
import com.playdata.pdfolio.oauth2.exception.NotSupportedOauth2Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    NO_MATCHING_SKILL_TYPE(HttpStatus.BAD_REQUEST, "해당 기술 스택은 존재하지 않습니다.", NoSuchSkillException.class),
    INVALID_URL_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않는 URL 타입입니다.", InValidUrlTypeException.class),

    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러"),
    COLUMN_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 컬럼입니다.", ColumnAlreadyDeletedException.class),
    NOT_SUPPORTED_OAUTH2(HttpStatus.BAD_REQUEST, "지원하지 않는 Oauth2 입니다.", NotSupportedOauth2Exception.class),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "요청에서 토큰을 찾을 수 없습니다.", TokenNotFoundException.class),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.",InvalidTokenException .class),
    ACCESS_TOKEN_EXPIRE(HttpStatus.FORBIDDEN, "액세스 토큰이 만료되었습니다.", AccessTokenExpiredException.class),
    UNREGISTERED_MEMBER(HttpStatus.ACCEPTED, "가입되지 않은 멤버입니다.", UnregisteredMemberException.class),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "멤버를 찾을 수 없습니댜.", MemberNotFoundException.class),

    INVALID_PROJECT_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 프로젝트 검색 타입입니다.",InValidProjectSearchTypeException .class),
    INVALID_PAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 페이지입니다.", InvalidPageException.class),
    INVALID_SIZE_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 사이즈입니다.", InvalidSizeException.class),
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "프로젝트를 찾을 수 없습니다.", ProjectNotFoundException.class),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private Class<? extends PdFolioException> type;

    ExceptionType(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ExceptionType of(Class<? extends PdFolioException> classType) {
        return Arrays.stream(values())
                .filter(eType -> Objects.nonNull(eType.type) && eType.type.equals(classType))
                .findFirst()
                .orElse(UNKNOWN_EXCEPTION);
    }
}
