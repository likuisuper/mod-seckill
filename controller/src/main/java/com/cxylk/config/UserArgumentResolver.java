package com.cxylk.config;

import com.cxylk.biz.SeckillUserService;
import com.cxylk.constant.ConstantField;
import com.cxylk.po.SeckillUser;
import com.cxylk.util.ComUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname UserArgumentResolver
 * @Description controller中具体参数类型的处理
 * @Author likui
 * @Date 2021/1/27 15:01
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private SeckillUserService seckillUserService;

    /**
     * 当参数类型是SeckillUser时才做处理
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数的类型
        Class<?> parameterType = methodParameter.getParameterType();
        //如果类型是SeckillUser则返回true
        return parameterType== SeckillUser.class;
    }

    /**
     * 具体的处理逻辑
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return 参数或者cookie中的token值
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //获取request请求
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        //获取response,为了延迟分布式session的有效期
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
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
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
