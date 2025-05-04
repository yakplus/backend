package com.likelion.backendplus4.yakplus.switcher.presentation.controller;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.switcher.application.port.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.switcher.presentation.controller.docs.EmbeddingRouterControllerDocs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * Embedding 라우팅 어댑터를 전환하고 조회하는 REST 컨트롤러
 *
 * 요청에 따라 활성화된 Embedding adapter를 변경하거나 현재 adapter를 확인합니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequestMapping("/switch/embeddings")
public class EmbeddingRouterController implements EmbeddingRouterControllerDocs {
    private final EmbeddingRoutingUseCase routerUseCase;

    public EmbeddingRouterController(EmbeddingRoutingUseCase routerUseCase) {
        this.routerUseCase = routerUseCase;
    }

    /**
     * 지정된 adapterBeanName에 해당하는 embedding adapter로 전환합니다.
     *
     * @param adapterBeanName 전환할 adapter Bean 이름
     * @return 어댑터 변경 결과 메시지를 담은 ApiResponse
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    @PostMapping("/switch/{adapterBeanName}")
    public ResponseEntity<ApiResponse<String>> switchAdapter(@PathVariable String adapterBeanName) {
        log("스위치 대상 인덱스명 : " + adapterBeanName);
        routerUseCase.switchEmbedding(adapterBeanName);
        return ApiResponse.success("어댑터 변경됨 - 어댑터명: " + adapterBeanName);
    }

    /**
     * 현재 활성화된 embedding adapter Bean 이름을 조회합니다.
     *
     * @return 현재 adapter Bean 이름을 담은 ApiResponse
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    @GetMapping("/current/adapter")
    public ResponseEntity<ApiResponse<String>> checkCurrentAdapter() {
        return ApiResponse.success(routerUseCase.getAdapterBeanName());
    }
}
