package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugSymptomService;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomList;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomSearchListResponse;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 약품 증상 데이터 색인 및 자동완성 기능을 제공하는 REST 컨트롤러입니다.
 *
 * @author 박찬병
 * @since 2025-04-24
 * @modified 2025-04-25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/symptom")
public class DrugSymptomController {

	private final DrugSymptomService indexService;

	/**
	 * 색인 배치 작업을 실행하여, DB로부터 조회한 약품 증상 데이터를 Elasticsearch에 일괄 색인합니다.
	 *
	 * @return 색인 작업 성공 여부 응답 (Void)
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	@PostMapping("/index")
	public ResponseEntity<ApiResponse<Void>> triggerIndex() {
		indexService.indexAll();
		return ApiResponse.success();
	}

	/**
	 * 사용자 입력 키워드를 바탕으로 증상 자동완성 추천 결과를 조회합니다.
	 *
	 * @param q 검색어 프리픽스
	 * @return 자동완성 추천 키워드 리스트를 감싼 ApiResponse
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	@GetMapping("/autocomplete")
	public ResponseEntity<ApiResponse<DrugSymptomSearchListResponse>> autocomplete(@RequestParam String q) {
		DrugSymptomSearchListResponse results = indexService.getSymptomAutoComplete(q);
		return ApiResponse.success(results);
	}

	/**
	 * 증상 키워드 검색으로 매칭되는 약품명 리스트를 반환합니다.
	 *
	 * @param q     검색어 프리픽스
	 * @param page  조회할 페이지 번호 (기본값 0)
	 * @param size  페이지 당 문서 수 (기본값 20)
	 * @return 약품명 리스트를 담은 ApiResponse
	 */
	@GetMapping("/search/names")
	public ResponseEntity<ApiResponse<DrugSymptomList>> searchNames(
		@RequestParam String q,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {
		DrugSymptomList drugSymptomList = indexService.searchDrugNamesBySymptom(q, page, size);
		return ApiResponse.success(drugSymptomList);
	}

}
