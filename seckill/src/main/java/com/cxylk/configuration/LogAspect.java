package com.cxylk.configuration;

import com.cxylk.exception.BizException;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname LogAspect
 * @Description 配置controller层日志切面
 * @Author likui
 * @Date 2021/1/28 17:31
 **/
@Aspect
@Slf4j
@Component
public class LogAspect {
    private long longTime = 10000;

    /**
     * 定义切入点，即定义何地
     */
    @Pointcut("execution(public * com.cxylk.controller.*.*(..))")
    public void log() {

    }

    /**
     * 执行方法前
     *
     * @param joinPoint 连接点
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取客户端ip
        String ip = request.getRemoteAddr();
        //获取客户端主机
        String host = request.getRemoteHost();
        //获取请求方法
        String method = request.getMethod();
        //获取端口
        int port = request.getRemotePort();
        //获取url
        String url = request.getRequestURL().toString();
        //记录client信息
        log.info("client:ip=" + ip + ";host=" + host + ";method=" + method + ";port=" + port + ";url=" + url);
        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //方法
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        //日志记录，类，方法，参数
        log.info("********class name:" + className + ",method name:" + methodName + "********");
    }

    @AfterReturning(value = "log()", returning = "keys")
    public void doAfterReturning(JoinPoint point, Object keys) {
        //获取类的全限定名
        String realName = point.getTarget().getClass().getName();
        log.info("********" + realName + "类执行" + point.getSignature().getName() + "方法********返回值:" + keys);
    }

    /**
     * 方法执行
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
        //创建一个秒表，用于对一段代码耗时检测
        StopWatch stopWatch = new StopWatch();
        Object result = null;
        try {
            stopWatch.start();
            //必须调用proceed方法，不然会导致目标方法没有执行
            result = proceedingJoinPoint.proceed();
            stopWatch.stop();
            //日志暂不记录参数，放在参数包含敏感信息
            long time = stopWatch.getTime();
            if (time > longTime) {
                log.info("执行方法:{},耗时:{}ms(毫秒),耗时较长", proceedingJoinPoint.getSignature(), time);
            } else {
                log.info("执行方法:{},耗时:{}ms(毫秒)", proceedingJoinPoint.getSignature(), time);
            }
        } catch (Throwable throwable) {
            result = handleException(proceedingJoinPoint, throwable);
        }
        return result;
    }

    /**
     * 后置异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "log()", throwing = "e")
    public void doException(Throwable e) {
        if (e != null) {
            log.error("doException系统异常" + e.getMessage(), e);
        }
    }

    private ResponseResult<?> handleException(ProceedingJoinPoint joinPoint, Throwable e) {
        log.error("Exception{方法:{},参数:{}}", joinPoint.getSignature(), joinPoint.getArgs());
        log.error(e.getMessage(), e);
        if (e instanceof BizException) {
            return Response.makeErrRsp(((BizException) e).getResultCode());
        } else {
            ResponseResult<?> responseResult = new ResponseResult<>();
            //返回消息暂定为服务器异常，根据需要自定义
            //服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器端的源代码出现错误时出现
            responseResult.setCode("500");
            responseResult.setMsg("后端接口错误");
            responseResult.setData(null);
            return responseResult;
        }
    }
}
