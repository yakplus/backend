package com.likelion.backendplus4.yakplus.common.configuration;

import com.likelion.backendplus4.yakplus.common.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 요청에 대해 LogInterceptor를 적용
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**");  // 모든 URL 패턴에 적용
    }
}
