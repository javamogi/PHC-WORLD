package com.phcworld.exception.model;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomException extends RuntimeException {
    private String statusCode;
    private Map<String, String> messages;

    public CustomException(String statusCode, String message){
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        this.statusCode = statusCode;
        this.messages = map;
    }

    public CustomException(String statusCode, List<ObjectError> errors) {
        Map<String, String> map = new HashMap<>();
        for(ObjectError error : errors) {
            map.put(error.getObjectName(), error.getDefaultMessage());
        }
        this.statusCode = statusCode;
        this.messages = map;
    }
}
