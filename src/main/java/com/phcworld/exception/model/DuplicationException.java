package com.phcworld.exception.model;

public class DuplicationException extends CustomBaseException{
    public DuplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public DuplicationException() {
        super(ErrorCode.CONFLICT);
    }
}
