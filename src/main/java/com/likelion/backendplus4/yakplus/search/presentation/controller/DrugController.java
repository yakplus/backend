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
 * @modified 2025-04-29
 * @since 2025-04-22
 */
@RestController
@RequestMapping("/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final SearchDrugUseCase searchDrugUseCase;

    /**
     * 약품 검색 요청을 처리하여 검색 결과를 반환한다.
     *
     * @param SearchRequest 검색어, 페이지 및 페이지 크기를 담은 요청 객체
     * @return 검색 결과 리스트를 포함한 표준 응답 구조
     * @author 정안식
     * @modified 2025-04-24
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
     * 사용자 입력 키워드를 바탕으로 자동완성 추천 결과를 조회합니다.
     * <p>
     * 검색 타입(type)에 따라 증상 자동완성 또는 약품명 자동완성 결과를 반환합니다.
     *
     * @param type 자동완성 타입 (symptom 또는 name)
     * @param q    검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
     * @throws SearchException 지원하지 않는 타입 입력 시 예외 발생
     * @author 박찬병
     * @modified 2025-04-29
     * @since 2025-04-24
     */
    @GetMapping("/autocomplete/{type}")
    public ResponseEntity<ApiResponse<AutoCompleteStringList>> autocomplete(
            @PathVariable String type,
            @RequestParam String q) {

        log("drugController 요청 수신 - type: " + type + ", query: " + q);

        SearchType searchType = SearchType.from(type);

        AutoCompleteStringList results = switch (searchType) {
            case SYMPTOM -> searchDrugUseCase.getSymptomAutoComplete(q);
            case NAME -> searchDrugUseCase.getDrugNameAutoComplete(q);
        };

        return ApiResponse.success(results);
    }

    /**
     * 증상 또는 약품명 검색을 통해 매칭되는 약품 리스트를 조회합니다.
     *
     * @param type 검색 타입 (symptom 또는 name)
     * @param q    검색어 프리픽스
     * @param page 조회할 페이지 번호 (기본값 0)
     * @param size 페이지 당 문서 수 (기본값 10)
     * @return 검색 결과를 담은 ApiResponse
     * @throws SearchException 지원하지 않는 검색 타입 입력 시 예외 발생
     * @author 박찬병
     * @modified 2025-04-29
     * @since 2025-04-24
     */
    @GetMapping("/search/{type}")
    public ResponseEntity<ApiResponse<SearchResponseList>> searchDrugs(
            @PathVariable String type,
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log("drugController 요청 수신 - type: " + type + ", query: " + q);

        SearchType searchType = SearchType.from(type);

        SearchResponseList results = switch (searchType) {
            case SYMPTOM -> searchDrugUseCase.searchDrugBySymptom(q, page, size);
            case NAME -> searchDrugUseCase.searchDrugByDrugName(q, page, size);
        };

        return ApiResponse.success(results);
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