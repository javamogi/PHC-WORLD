package com.phcworld.exception.handler;

import com.phcworld.exception.model.CustomBaseException;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.ErrorCode;
import com.phcworld.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CommonsExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handlerException(CustomException e) {
        Map<String, String> map = e.getMessages();
        if(e.getStatusCode().equals("400")){
            return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
        } else if(e.getStatusCode().equals("401")){
            return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
        } else if(e.getStatusCode().equals("403")){
            return new ResponseEntity(map, HttpStatus.FORBIDDEN);
        } else if(e.getStatusCode().equals("500")){
            return new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if(e.getStatusCode().equals("420")){
            return new ResponseEntity(map, HttpStatus.METHOD_FAILURE);
        }
        else {
            return new ResponseEntity(map, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<ErrorResponse> handle(CustomBaseException e){
        log.error("Exception");
        return createErrorResponseEntity(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getHttpStatus());
    }
}
