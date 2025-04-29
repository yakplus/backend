package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 약품 검색 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-04-28
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
        log("drugController 요청 수신" + searchRequest.toString());
        return ApiResponse.success(searchDrugUseCase.search(searchRequest));
    }

    /**
     * 사용자 입력 키워드를 바탕으로 증상 자동완성 추천 결과를 조회합니다.
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-28
     */
    @GetMapping("/autocomplete/{type}")
    public ResponseEntity<ApiResponse<AutoCompleteStringList>> autocomplete(
        @PathVariable String type,
        @RequestParam String q) {

        log("drugController 요청 수신 - type: " + type + ", query: " + q);

        AutoCompleteStringList results;

        switch (type.toLowerCase()) {
            case "symptom" -> results = searchDrugUseCase.getSymptomAutoComplete(q);
            case "name" -> results = searchDrugUseCase.getDrugNameAutoComplete(q);
            default -> throw new IllegalArgumentException("지원하지 않는 자동완성 타입입니다: " + type);
        }

        return ApiResponse.success(results);
    }

    /**
     * 증상 또는 약품명 검색을 통해 매칭되는 약품 리스트를 조회합니다.
     *
     * @param type  검색 타입 (symptom 또는 name)
     * @param q     검색어 프리픽스
     * @param page  조회할 페이지 번호 (기본값 0)
     * @param size  페이지 당 문서 수 (기본값 10)
     * @return 검색 결과를 담은 ApiResponse
     * @throws IllegalArgumentException 지원하지 않는 검색 타입 입력 시 예외 발생
     * @since 2025-04-24
     * @modified 2025-04-30
     */
    @GetMapping("/search/{type}")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchDrugs(
        @PathVariable String type,
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        log("drugController 요청 수신 - type: " + type + ", query: " + q);

        SearchResponseList result;

        switch (type.toLowerCase()) {
            case "symptom" -> result = searchDrugUseCase.searchDrugBySymptom(q, page, size);
            case "name" -> result = searchDrugUseCase.searchDrugByDrugName(q, page, size);
            default -> throw new IllegalArgumentException("지원하지 않는 검색 타입입니다: " + type);
        }

        return ApiResponse.success(result);
    }

}