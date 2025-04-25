package com.likelion.backendplus4.yakplus.index.presentation.controller;

import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request.IndexRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 약품 인덱싱 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-04-25
 * @since 2025-04-22
 */
@RestController
@RequestMapping("/api/drugs/index")
@RequiredArgsConstructor
public class DrugController {
    private final IndexUseCase indexUseCase;

    /**
     * 색인 생성 요청을 처리한다.
     *
     * @param request 인덱싱 범위 및 개수 정보를 담은 요청 객체
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    @PostMapping("/index")
    public void index(@RequestBody IndexRequest request) {
        indexUseCase.index(request);
    }
}