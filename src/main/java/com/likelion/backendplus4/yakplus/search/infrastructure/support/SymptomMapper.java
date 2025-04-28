package com.likelion.backendplus4.yakplus.search.infrastructure.support;


import com.likelion.backendplus4.yakplus.search.domain.model.DrugSymptom;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;

/**
 * 증상 관련 Document를 다루는 매퍼 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-25
 * @modified 2025-04-25
 */
public class SymptomMapper {

	/**
	 * ES 색인용 Document를 도메인 모델(DrugSymptom)로 변환합니다.
	 *
	 * @param symptomDocument 변환 대상 ES Document 객체
	 * @return DrugSymptom 도메인 모델 객체
	 * @author 박찬병
	 * @since 2025-04-25
	 * @modified 2025-04-25
	 */
	public static DrugSymptom toDomain(DrugSymptomDocument symptomDocument) {
		return DrugSymptom.builder()
			.drugId(symptomDocument.getDrugId())
			.drugName(symptomDocument.getDrugName())
			.efficacy(symptomDocument.getEfficacy())
			.company(symptomDocument.getCompany())
			.imageUrl(symptomDocument.getImageUrl())
			.build();
	}


	/**
	 * 도메인 모델(DrugSymptom)을 HTTP 응답용 DTO(DrugSymptomResponse)로 변환합니다.
	 *
	 * @param drugSymptom 변환 대상 도메인 객체
	 * @return DrugSymptomResponse 응답 DTO 객체
	 * @author 박찬병
	 * @since 2025-04-25
	 * @modified 2025-04-25
	 */
	public static SearchResponse toResponse(DrugSymptom drugSymptom) {
		return SearchResponse.builder()
			.drugId(drugSymptom.getDrugId())
			.drugName(drugSymptom.getDrugName())
			.efficacy(drugSymptom.getEfficacy())
			.company(drugSymptom.getCompany())
			.imageUrl(drugSymptom.getImageUrl())
			.build();
	}
}
