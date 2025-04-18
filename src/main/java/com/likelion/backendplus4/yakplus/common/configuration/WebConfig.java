package com.likelion.backendplus4.yakplus.common.configuration;

import com.likelion.backendplus4.yakplus.common.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 설정을 위한 설정 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String ALL_PATTERN = "/**";

    private LogInterceptor logInterceptor;

    /**
     * 인터셉터를 등록하는 메서드
     *
     * @param registry InterceptorRegistry 인터셉터 레지스트리
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 요청에 대해 LogInterceptor를 적용
        registry.addInterceptor(logInterceptor)
                .addPathPatterns(ALL_PATTERN);  // 모든 URL 패턴에 적용
    }
}
