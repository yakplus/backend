package com.likelion.backendplus4.yakplus.search.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * 약품 검색 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Drug", description = "약품 검색 및 자동완성 API")
public interface DrugControllerDocs {

	@Operation(summary = "자연어 검색", description = "자연어 문장으로 약품을 검색합니다.")
	ResponseEntity<ApiResponse<SearchResponseList>> searchNatural(
		@Parameter(in = ParameterIn.QUERY, description = "검색 쿼리", example = "머리가 아파요") String q,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 번호", example = "0") int page,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 크기", example = "10") int size
	);

	@Operation(summary = "증상 자동완성", description = "증상 키워드 자동완성 추천 리스트를 반환합니다.")
	ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteSymptom(
		@Parameter(in = ParameterIn.QUERY, description = "검색어 프리픽스", example = "두통") String q
	);

	@Operation(summary = "약품명 자동완성", description = "약품명 키워드 자동완성을 수행합니다.")
	ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteDrugName(
		@Parameter(in = ParameterIn.QUERY, description = "검색어 프리픽스", example = "타이레") String q
	);

	@Operation(summary = "성분명 자동완성", description = "성분명 키워드 자동완성을 수행합니다.")
	ResponseEntity<ApiResponse<AutoCompleteStringList>> autocompleteIngredient(
		@Parameter(in = ParameterIn.QUERY, description = "검색어 프리픽스", example = "아세트") String q
	);

	@Operation(summary = "증상 기반 키워드 검색", description = "증상 키워드로 약품 리스트를 검색합니다.")
	ResponseEntity<ApiResponse<SearchResponseList>> searchBySymptom(
		@Parameter(in = ParameterIn.QUERY, description = "검색 키워드", example = "두통") String q,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 번호", example = "0") int page,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 크기", example = "10") int size
	);

	@Operation(summary = "약품명 기반 키워드 검색", description = "약품명 키워드로 약품 리스트를 검색합니다.")
	ResponseEntity<ApiResponse<SearchResponseList>> searchByName(
		@Parameter(in = ParameterIn.QUERY, description = "검색 키워드", example = "타이레놀") String q,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 번호", example = "0") int page,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 크기", example = "10") int size
	);

	@Operation(summary = "성분명 기반 키워드 검색", description = "성분명 키워드로 약품 리스트를 검색합니다.")
	ResponseEntity<ApiResponse<SearchResponseList>> searchByIngredient(
		@Parameter(in = ParameterIn.QUERY, description = "검색 키워드", example = "아세트아미노펜") String q,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 번호", example = "0") int page,
		@Parameter(in = ParameterIn.QUERY, description = "페이지 크기", example = "10") int size
	);

	@Operation(summary = "의약품 상세 조회", description = "의약품 ID로 상세 정보를 조회합니다.")
	ResponseEntity<ApiResponse<DetailSearchResponse>> searchDetail(
		@Parameter(in = ParameterIn.PATH, description = "의약품 고유 ID", example = "12345") Long id
	);
}