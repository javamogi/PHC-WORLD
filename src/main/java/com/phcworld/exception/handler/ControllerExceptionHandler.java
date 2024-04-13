package com.phcworld.exception.handler;

import com.phcworld.exception.model.*;
import com.phcworld.user.controller.port.SessionUser;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@Slf4j
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

    @ExceptionHandler(FreeBoardNotFoundException.class)
    public ModelAndView notFoundFreeBoard(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "게시물이 존재하지 않습니다.");
        mav.setViewName("redirect:/freeboards");
        return mav;
    }

    @ExceptionHandler({ConstraintViolationException.class, BindException.class})
    public ModelAndView handle(Exception e, HttpSession session){
        boolean bool = HttpSessionUtils.isLoginUser(session);
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
        if(bool){
//            UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
            SessionUser loginUser = HttpSessionUtils.getUserFromSession(session);
            mav.addObject("user", loginUser);
            mav.setViewName("user/updateForm");
        } else {
            mav.setViewName("user/form");
        }
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
        mav.addObject("errorMessage", "인증코드가 틀립니다.");
        mav.setViewName("user/form");
        return mav;
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ModelAndView notMatchPassword(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "비밀번호가 틀립니다.");
        mav.setViewName("user/login");
        return mav;
    }

    @ExceptionHandler(LoginUserNotFoundException.class)
    public ModelAndView notFoundLoginUser(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "가입되지 않은 이메일입니다.");
        mav.setViewName("user/login");
        return mav;
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ModelAndView unauthenticateUser(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "이메일 인증이 되지 않은 계정입니다.");
        mav.setViewName("user/login");
        return mav;
    }

    @ExceptionHandler(NotMatchUserException.class)
    public ModelAndView notMatchWriter(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
        mav.setViewName("user/login");
        return mav;
    }

}
