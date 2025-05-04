package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.natural.SearchNaturalMapper;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 약품 검색 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-05-02
 * @since 2025-04-22
 */
@RestController
@RequestMapping("/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final SearchDrugUseCase searchDrugUseCase;

    /**
     * 자연어 기반 검색을 처리합니다.
     *
     * @param query 검색 쿼리 문자열
     * @param page  조회할 페이지 번호 (기본값 0)
     * @param size  페이지당 결과 개수 (기본값 10)
     * @return 검색 결과를 담은 표준 API 응답
     * @author 정안식
     * @modified 2025-05-02
     * - GET 메소드로 변경, 이제 Service에 넘길 때 SearchRequest를 사용하지 않음
     * @since 2025-04-22
     */
    @GetMapping("/search/natural")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchNatural(
            @RequestParam("q") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log("drugController 자연어 검색 요청 수신 - 자연어: " + query);
        DrugSearchNatural natural = SearchNaturalMapper.toDrugSearchNatural(query, page, size);
        return ApiResponse.success(searchDrugUseCase.searchDrugByNatural(natural));
    }

    /**
     * 증상 키워드 자동완성 API
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/autocomplete/symptom")
    public ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteSymptom(@RequestParam String q) {
        log("drugController 요청 수신 - symptom autocomplete, query: " + q);
        AutoCompleteStringList results = searchDrugUseCase.getSymptomAutoComplete(q);
        return ApiResponse.success(results);
    }

    /**
     * 약품명 자동완성 API
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/autocomplete/name")
    public ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteDrugName(@RequestParam String q) {
        log("drugController 요청 수신 - drug name autocomplete, query: " + q);
        AutoCompleteStringList results = searchDrugUseCase.getDrugNameAutoComplete(q);
        return ApiResponse.success(results);
    }

    /**
     * 성분명 자동완성 API
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/autocomplete/ingredient")
    public ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteIngredient(@RequestParam String q) {
        log("drugController 요청 수신 - ingredient autocomplete, query: " + q);
        AutoCompleteStringList results = searchDrugUseCase.getIngredientAutoComplete(q);
        return ApiResponse.success(results);
    }

    /**
     * 증상 기반 키워드 검색 API
     *
     * @param query 검색 키워드
     * @param page  페이지 번호 (기본값 0)
     * @param size  페이지당 개수 (기본값 10)
     * @return 증상 기반 검색 결과 리스트
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/search/symptom")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchBySymptom(
            @RequestParam("q") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log("drugController 증상 검색 요청 수신 - query: " + query);
        DrugSearchNatural natural = SearchNaturalMapper.toDrugSearchNatural(query, page, size);
        return ApiResponse.success(searchDrugUseCase.searchDrugBySymptom(natural));
    }

    /**
     * 약품명 기반 키워드 검색 API
     *
     * @param query 검색 키워드
     * @param page  페이지 번호 (기본값 0)
     * @param size  페이지당 개수 (기본값 10)
     * @return 약품명 기반 검색 결과 리스트
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchByName(
            @RequestParam("q") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log("drugController 약품명 검색 요청 수신 - query: " + query);
        DrugSearchNatural natural = SearchNaturalMapper.toDrugSearchNatural(query, page, size);
        return ApiResponse.success(searchDrugUseCase.searchDrugByDrugName(natural));
    }

    /**
     * 성분명 기반 키워드 검색 API
     *
     * @param query 검색 키워드
     * @param page  페이지 번호 (기본값 0)
     * @param size  페이지당 개수 (기본값 10)
     * @return 성분명 기반 검색 결과 리스트
     * @author 박찬병
     * @since 2025-05-03
     */
    @GetMapping("/search/ingredient")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchByIngredient(
            @RequestParam("q") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        log("drugController 성분 검색 요청 수신 - query: " + query);
        DrugSearchNatural natural = SearchNaturalMapper.toDrugSearchNatural(query, page, size);
        return ApiResponse.success(searchDrugUseCase.searchDrugByIngredient(natural));
    }

    /**
     * 의약품 ID를 통해 상세 정보를 조회하는 API입니다.
     *
     * @param id 조회할 의약품의 고유 ID
     * @return 의약품 상세 정보가 담긴 ApiResponse
     * @author 함예정
     * @since 2025-04-30
     */
    @GetMapping("/search/detail/{id}")
    public ResponseEntity<ApiResponse<DetailSearchResponse>> searchDetail(@PathVariable Long id) {
        return ApiResponse.success(searchDrugUseCase.searchByDrugId(id));
    }

}