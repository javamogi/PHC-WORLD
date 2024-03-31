package com.phcworld.exception.handler;

import com.phcworld.exception.model.BadRequestException;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.EmailSendErrorException;
import com.phcworld.exception.model.NotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DuplicationException.class)
    public ModelAndView duplicateEmail(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "이미 등록된 이메일입니다.");
        mav.setViewName("user/form");
        return mav;
    }

    @ExceptionHandler(EmailSendErrorException.class)
    public ModelAndView failedSendEmail(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "인증 이메일 보내기 실패. 다시 시도하세요.");
        mav.setViewName("user/form");
        return mav;
    }

    @ExceptionHandler({ConstraintViolationException.class, BindException.class})
    public ModelAndView handle(Exception e){
        ModelAndView mav = new ModelAndView();
        if(e instanceof ConstraintViolationException){
            ConstraintViolationException cve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                mav.addObject("errorMessage", constraintViolation.getMessage());
            }
        } else {
            BindException be = (BindException) e;
            List<ObjectError> errors = be.getAllErrors();
            for (ObjectError error : errors) {
                mav.addObject("errorMessage", error.getDefaultMessage());
            }
        }
        mav.setViewName("user/form");
        return mav;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundUser(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "가입되지 않은 이메일입니다.");
        mav.setViewName("user/form");
        return mav;
    }

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView notMatchCertificationCode(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "인증키가 틀립니다.");
        mav.setViewName("user/form");
        return mav;
    }

}
