package com.phcworld.exception.model;

public class AnswerNotFoundException extends CustomBaseException{
    public AnswerNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public AnswerNotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
