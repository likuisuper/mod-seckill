package com.cxylk.config;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname AopConfig
 * @Description impl切面配置
 * @Author likui
 * @Date 2021/1/29 9:46
 **/
@Aspect
@Configuration
@Slf4j
public class AopConfig {
    /**
     * 切入点
     */
    @Pointcut("execution(public * com.cxylk.biz.*.*(..))")
    public void aopLog() {
        //soNothing
    }

    /**
     * 前置通知
     *
     * @param joinPoint 连接点
     */
    @Before("aopLog()")
    public void invokeBefore(JoinPoint joinPoint) {
        String realClass = getRealClassName(joinPoint);
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        log.info("*****" + realClass + "类执行" + getMethod(joinPoint) + "方法之前，传入参数为:" + args + "*****");
    }

    /**
     * 后置通知
     *
     * @param joinPoint 连接点
     */
    @After("aopLog()")
    public void after(JoinPoint joinPoint) {
        String realClassName = getRealClassName(joinPoint);
        log.info("*****" + realClassName + "类执行" + getMethod(joinPoint) + "方法之后" + "*****");
    }

    /**
     * 后置返回通知
     *
     * @param joinPoint 连接点
     * @param keys      返回结果
     */
    @AfterReturning(value = "aopLog()", returning = "keys")
    public void afterReturning(JoinPoint joinPoint, Object keys) {
        String realClassName = getRealClassName(joinPoint);
        log.info("*****" + realClassName + "类执行" + getMethod(joinPoint) + "方法之后，返回:" + keys + "*****");
    }

    /**
     * 后置异常通知
     *
     * @param joinPoint 连接点
     * @param e         异常
     */
    @AfterThrowing(value = "execution(* com.cxylk.biz.*.*(..))", throwing = "e")
    public void throwsException(JoinPoint joinPoint, Throwable e) {
        String realClassName = getRealClassName(joinPoint);
        log.info("*****" + "Exception所在方法" + realClassName + "." + getMethod(joinPoint) + "Exception信息:" + e.getMessage());
    }

    /**
     * 返回被代理类
     *
     * @param joinPoint 连接点
     * @return 被代理类
     */
    private String getRealClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    /**
     * 返回被代理方法
     *
     * @param joinPoint 连接点
     * @return 被代理方法
     */
    private String getMethod(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
