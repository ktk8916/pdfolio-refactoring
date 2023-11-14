package com.playdata.pdfolio.global.exception;

import com.playdata.pdfolio.project.exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    INVALID_URL_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않는 URL 타입입니다.", InValidUrlTypeException.class),

    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러"),



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
