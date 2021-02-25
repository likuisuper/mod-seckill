package com.cxylk.access;

import com.alibaba.fastjson.JSON;
import com.cxylk.biz.SeckillUserService;
import com.cxylk.constant.ConstantField;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.Response;
import com.cxylk.response.ResultCode;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.AccessKey;
import com.cxylk.util.ComUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
/**
 * @Classname AccessInterceptor
 * @Description TODO
 * @Author likui
 * @Date 2021/2/25 21:40
 **/
@Service
public class AccessInterceptor implements HandlerInterceptor {
    @Autowired
    private SeckillUserService seckillUserService;

    @Autowired
    private RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            SeckillUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod method = (HandlerMethod) handler;
            //获取方法上的注解
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            //如果没加注解正常通过
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            //如果需要登录
            if(needLogin){
                //但是用户为空
                if(user==null){
                    //给客户端提示
                    render(response,ResultCode.SESSION_ERROR);
                    return false;
                }
                key+="_"+user.getId();
            }else {
                //do nothing
            }
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if(count==null){
                redisService.set(ak,key,1);
            }else if(count<maxCount){
                redisService.incr(ak,key);
            }else {
                render(response,ResultCode.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求处理完成后回调该方法清除ThreadLocal中的user，防止内存泄露
        UserContext.removeUser();
    }

    /**
     * 返回错误码给前端
     * @param response
     * @param codeMsg
     * @throws IOException
     */
    private void render(HttpServletResponse response, ResultCode codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        String result = JSON.toJSONString(Response.makeErrRsp(codeMsg));
        outputStream.write(result.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) throws BizException {
        //获取参数中的token，防止出现token没有放在cookie而是放在请求头的情况
        String parameterToken = request.getParameter(ConstantField.COOKIE_NAME_TOKEN);
        //获取cookie中的token
        String cookieToken=getCookieValue(request,ConstantField.COOKIE_NAME_TOKEN);
        if(ComUtil.isEmpty(cookieToken)&&ComUtil.isEmpty(parameterToken)){
            return null;
        }
        //参数中的token不为空就从参数中获取，不然从cookie中获取
        String token=ComUtil.isEmpty(parameterToken)?cookieToken:parameterToken;
        return seckillUserService.getByToken(response,token);
    }

    /**
     * 获取cookie中的值
     * @param request
     * @param cookieName
     * @return
     */
    private String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(ComUtil.isEmpty(cookies)){
            return null;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
