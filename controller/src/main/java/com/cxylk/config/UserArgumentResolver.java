package com.cxylk.config;

import com.cxylk.access.UserContext;
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
        //因为是先走拦截器再走参数，而在拦截器里面已经获取user并保存了，所以这里直接获取
        return UserContext.getUser();
    }

}
