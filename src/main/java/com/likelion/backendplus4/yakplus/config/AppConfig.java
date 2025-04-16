package com.likelion.backendplus4.yakplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig는 애플리케이션에서 필요한 공통 빈들을 등록하는 구성 클래스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate 빈을 생성하여 애플리케이션 컨텍스트에 등록한다.
     *
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
