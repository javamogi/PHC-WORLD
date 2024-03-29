package com.phcworld.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.exception.model.CustomBaseException;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.ErrorCode;
import com.phcworld.exception.model.ErrorResponse;
import com.phcworld.utils.SlackErrorMessage;
import com.slack.api.Slack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CommonsExceptionHandler {

    @Value("${notification.slack.webhook.url}")
    private String slackAlertWebhookUrl;

    private final Slack slack;

    private final ObjectMapper objectMapper;

//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity handlerException(CustomException e) throws IOException {
//        Map<String, String> map = e.getMessages();
//        if(e.getStatusCode().equals("400")){
//            return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
//        } else if(e.getStatusCode().equals("401")){
//            return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
//        } else if(e.getStatusCode().equals("403")){
//            String errorMessage = e.getMessage();
//            SlackErrorMessage message = new SlackErrorMessage(errorMessage);
//            slack.send(slackAlertWebhookUrl, objectMapper.writeValueAsString(message));
//
//            return new ResponseEntity(map, HttpStatus.FORBIDDEN);
//        } else if(e.getStatusCode().equals("500")){
//            return new ResponseEntity(map, HttpStatus.INTERNAL_SERVER_ERROR);
//        } else if(e.getStatusCode().equals("420")){
//            return new ResponseEntity(map, HttpStatus.METHOD_FAILURE);
//        }
//        else {
//            return new ResponseEntity(map, HttpStatus.NOT_FOUND);
//        }
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handlerBadCredentialsException(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", "비밀번호가 틀렸습니다.");
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(CustomBaseException.class)
//    public ResponseEntity<ErrorResponse> handle(CustomBaseException e){
//        log.error("Exception");
//        return createErrorResponseEntity(e.getErrorCode());
//    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity handle(BindException e){
        log.error("Exception");
        Map<String, Object> map = new HashMap<>();
        List<FieldError> errors = e.getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (int i = 0; i < errors.size(); i++){
            FieldError error = errors.get(i);
            errorMessages.add(error.getDefaultMessage());
        }
        map.put("messages", errorMessages);
        return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getHttpStatus());
    }
}
