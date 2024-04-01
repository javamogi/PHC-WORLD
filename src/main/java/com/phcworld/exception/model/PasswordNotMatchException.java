package com.phcworld.exception.model;

public class PasswordNotMatchException extends CustomBaseException{
    public PasswordNotMatchException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public PasswordNotMatchException(){
        super(ErrorCode.BAD_REQUEST);
    }
}
