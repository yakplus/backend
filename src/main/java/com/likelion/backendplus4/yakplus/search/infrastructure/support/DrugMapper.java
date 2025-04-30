package com.likelion.backendplus4.yakplus.search.infrastructure.support;


import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugNameDocument;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;

/**
 * 증상 관련 객체를 다루는 매퍼 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-25
 * @modified 2025-04-29
 */
public class DrugMapper {

	/**
	 * ES 색인용 Document를 도메인 모델(DrugSymptom)로 변환합니다.
	 *
	 * @param symptomDocument 변환 대상 ES Document 객체
	 * @return DrugSymptom 도메인 모델 객체
	 * @author 박찬병
	 * @since 2025-04-25
	 * @modified 2025-04-25
	 */
	public static DrugSearchDomain toDomainBySymptomDocument(DrugSymptomDocument symptomDocument) {
		return DrugSearchDomain.builder()
			.drugId(symptomDocument.getDrugId())
			.drugName(symptomDocument.getDrugName())
			.efficacy(symptomDocument.getEfficacy())
			.company(symptomDocument.getCompany())
			.imageUrl(symptomDocument.getImageUrl())
			.build();
	}

	/**
	 * ES 색인용 Document를 도메인 모델(DrugSearchDomain)로 변환합니다.
	 *
	 * @param nameDocument 변환 대상 ES Document 객체 (약품명 전용)
	 * @return DrugSearchDomain 도메인 모델 객체
	 */
	public static DrugSearchDomain toDomainByNameDocument(DrugNameDocument nameDocument) {
		return DrugSearchDomain.builder()
			.drugId(nameDocument.getDrugId())
			.drugName(nameDocument.getDrugName())
			.efficacy(nameDocument.getEfficacy())
			.company(nameDocument.getCompany())
			.imageUrl(nameDocument.getImageUrl())
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
	public static SearchResponse toResponse(DrugSearchDomain drugSymptom) {
		return SearchResponse.builder()
			.drugId(drugSymptom.getDrugId())
			.drugName(drugSymptom.getDrugName())
			.efficacy(drugSymptom.getEfficacy())
			.company(drugSymptom.getCompany())
			.imageUrl(drugSymptom.getImageUrl())
			.build();
	}
}
