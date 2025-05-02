package com.likelion.backendplus4.yakplus.switcher.presentation.controller;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.switcher.application.port.in.EmbeddingRoutingUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@RestController
@RequestMapping("/switch/embeddings")
public class EmbeddingRouterController {
    private final EmbeddingRoutingUseCase routerUseCase;

    public EmbeddingRouterController(EmbeddingRoutingUseCase routerUseCase) {
        this.routerUseCase = routerUseCase;
    }

    @PostMapping("/switch/{adapterBeanName}")
    public ResponseEntity<ApiResponse<String>> switchAdapter(@PathVariable String adapterBeanName) {
        log("스위치 대상 인덱스명 : " + adapterBeanName);
        routerUseCase.switchEmbedding(adapterBeanName);
        return ApiResponse.success("어댑터 변경됨 - 어댑터명: " + adapterBeanName);
    }

    @GetMapping("/current/adapter")
    public ResponseEntity<ApiResponse<String>> checkCurrentAdapter() {
        return ApiResponse.success(routerUseCase.getAdapterBeanName());
    }
}
