package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.search.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.IndexRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 약품 검색 및 인덱싱 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @since 2025-04-22
 * @modified 2025-04-25
 */
@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final IndexUseCase indexUseCase;
    private final SearchDrugUseCase searchDrugUseCase;

    /**
     * 색인 생성 요청을 처리한다.
     *
     * @param request 인덱싱 범위 및 개수 정보를 담은 요청 객체
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    @PostMapping("/index")
    public void index(@RequestBody IndexRequest request) {
        indexUseCase.index(request);
    }

    /**
     * 약품 검색 요청을 처리하여 검색 결과를 반환한다.
     *
     * @param searchRequest 검색어, 페이지 및 페이지 크기를 담은 요청 객체
     * @return 검색 결과 리스트를 포함한 표준 응답 구조
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> search(@RequestBody SearchRequest searchRequest) {
        System.out.println("Controller 요청 정상수신: "+ searchRequest);
        return ApiResponse.success(searchDrugUseCase.search(searchRequest));
    }
}