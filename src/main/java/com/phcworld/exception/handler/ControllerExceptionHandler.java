package com.phcworld.exception.handler;

import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.EmailSendErrorException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

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

}
