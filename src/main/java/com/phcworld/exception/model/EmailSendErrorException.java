package com.phcworld.exception.model;

public class EmailSendErrorException extends CustomBaseException{
    public EmailSendErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public EmailSendErrorException(){
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
