package com.example.demo12.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    // 这里注入的是自定义的拦截器
    private JwtInterceptor jwtInterceptor;
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //url+api
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }
    @Override
    // 这里注册自定义的拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/test/login")
                .excludePathPatterns("/api/file/**")
                .excludePathPatterns("/api/test/upload")
                .excludePathPatterns("/api/department/upload")
                .excludePathPatterns("/api/overtime/upload")
                .excludePathPatterns("/api/attendance/upload")
                .excludePathPatterns("/api/position/upload")
                .excludePathPatterns("/api/test/register");
    }
}
