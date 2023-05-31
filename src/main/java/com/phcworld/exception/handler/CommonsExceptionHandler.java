package com.phcworld.exception.handler;

import com.phcworld.exception.model.CustomException;
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
}
