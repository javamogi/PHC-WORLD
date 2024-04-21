package com.phcworld.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class TimeAspect {

    @Around("com.phcworld.common.aop.Pointcuts.allFreeBoard()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("TimeAspect 실행");
        log.info("[log] {}", joinPoint.getSignature());

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeAspect 종료 resultTime={}", resultTime);
        return result;
    }
}
