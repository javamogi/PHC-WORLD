package com.phcworld.exception.model;

public class EmptyContentsException extends CustomBaseException{
    public EmptyContentsException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public EmptyContentsException(){
        super(ErrorCode.BAD_REQUEST);
    }
}
