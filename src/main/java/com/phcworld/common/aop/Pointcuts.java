package com.phcworld.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("execution(* com.phcworld.freeboard..*(..))")
    public void allFreeBoard(){}
}
