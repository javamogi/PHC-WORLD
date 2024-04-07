package com.phcworld.exception.model;

public class LoginUserNotFoundException extends CustomBaseException{
    public LoginUserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public LoginUserNotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
