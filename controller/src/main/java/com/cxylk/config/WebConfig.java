package com.cxylk.config;

import com.cxylk.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Classname WebConfig
 * @Description 自定义参数解析器，改变controller的传入参数
 * @Author likui
 * @Date 2021/1/27 15:00
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    private AccessInterceptor accessInterceptor;

    /**
     * 框架会回调该方法，然后给controller的参数赋值
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //添加一个实现了HandlerMethodArgumentResolver接口的类
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将拦截器注册进来
        registry.addInterceptor(accessInterceptor);
    }
}
