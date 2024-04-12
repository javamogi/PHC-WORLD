package com.phcworld.exception.model;

public class FreeBoardNotFoundException extends CustomBaseException{
    public FreeBoardNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public FreeBoardNotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
