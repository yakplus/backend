package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 약품 검색 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-04-25
 * @since 2025-04-22
 */
@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final SearchDrugUseCase searchDrugUseCase;

    /**
     * 약품 검색 요청을 처리하여 검색 결과를 반환한다.
     *
     * @param searchRequest 검색어, 페이지 및 페이지 크기를 담은 요청 객체
     * @return 검색 결과 리스트를 포함한 표준 응답 구조
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> search(@RequestBody SearchRequest searchRequest) {
        return ApiResponse.success(searchDrugUseCase.search(searchRequest));
    }
}