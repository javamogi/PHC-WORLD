package com.phcworld.exception.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private Integer statusCode;

    public ErrorResponse(ErrorCode code){
        this.message = code.getMessage();
        this.statusCode = code.getHttpStatus().value();
    }

    public ErrorResponse(ErrorCode code, String message){
        this.message = message;
        this.statusCode = code.getHttpStatus().value();
    }

    public static ErrorResponse of(ErrorCode code){
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message){
        return new ErrorResponse(code, message);
    }
}
