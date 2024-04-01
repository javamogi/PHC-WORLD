package com.phcworld.exception.model;

public class UnauthenticatedException extends CustomBaseException{
    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public UnauthenticatedException(){
        super(ErrorCode.BAD_REQUEST);
    }
}
