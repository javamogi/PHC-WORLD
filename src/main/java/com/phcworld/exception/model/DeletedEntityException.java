package com.phcworld.exception.model;

public class DeletedEntityException extends CustomBaseException{
    public DeletedEntityException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public DeletedEntityException() {
        super(ErrorCode.CONFLICT);
    }
}
