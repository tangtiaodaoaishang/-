package com.example.config;

import com.example.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求，通过判断是否token是否合法来决定是否需要登录
                //登录,注册,导入,导出,导入都是不携带token验证的,将这些请求从拦截器中删除
                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import","/file/**","menu/**","/user/**");

    }
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
