package com.phcworld.exception.model;

public class EmptyLoginUserException extends CustomBaseException{
    public EmptyLoginUserException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public EmptyLoginUserException(){
        super(ErrorCode.BAD_REQUEST);
    }
}
