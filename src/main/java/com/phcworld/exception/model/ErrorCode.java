package com.phcworld.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("존재하지 않는 엔티티입니다.", HttpStatus.NOT_FOUND);

    private final String message;

    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
