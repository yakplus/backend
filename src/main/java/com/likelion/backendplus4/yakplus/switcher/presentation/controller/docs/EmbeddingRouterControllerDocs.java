package com.likelion.backendplus4.yakplus.switcher.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * Embedding 라우팅 어댑터 전환/조회 API 문서 정의 인터페이스
 *
 * @since 2025-05-02
 */
@Tag(
	name = "Embedding Router",
	description = "어떤 Embedding adapter가 활성화되어 있는지 조회하고 전환하는 API"
)
public interface EmbeddingRouterControllerDocs {

	@Operation(
		summary = "Embedding 어댑터 전환",
		description = "지정된 adapterBeanName 으로 활성화된 Embedding adapter를 변경합니다."
	)
	ResponseEntity<ApiResponse<String>> switchAdapter(
		@Parameter(
			in = ParameterIn.PATH,
			description = "전환할 adapter Bean 이름",
			example = "KmBertEmbeddingAdapter"
		)
		String adapterBeanName
	);

	@Operation(
		summary = "현재 활성화된 Embedding 어댑터 조회",
		description = "현재 사용 중인 Embedding adapter Bean 이름을 반환합니다."
	)
	ResponseEntity<ApiResponse<String>> checkCurrentAdapter();
}