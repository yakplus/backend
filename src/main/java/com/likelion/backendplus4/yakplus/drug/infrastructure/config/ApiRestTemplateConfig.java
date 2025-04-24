package com.likelion.backendplus4.yakplus.drug.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Api 요청을 보내기 위한 RestTemplate 빈 생성
 *
 * @since 2025-04-15
 * @author 함예정
 */
@Configuration
public class ApiRestTemplateConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
