package com.likelion.backendplus4.yakplus.search.infrastructure.support;


import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByKeywordParams;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugKeywordDocument;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.entity.DrugEntity;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;

import co.elastic.clients.elasticsearch.core.search.Hit;

/**
 * 증상 관련 객체를 다루는 매퍼 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-25
 * @modified 2025-05-03
 */
public class DrugMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * ES 색인용 Document를 도메인 모델(DrugSymptom)로 변환합니다.
	 *
	 * @param symptomDocument 변환 대상 ES Document 객체
	 * @return DrugSymptom 도메인 모델 객체
	 * @author 박찬병
	 * @since 2025-04-25
	 * @modified 2025-05-01
	 */
	public static DrugSearchDomain toDomainByDocument(DrugKeywordDocument symptomDocument) {
		return DrugSearchDomain.builder()
				.drugId(symptomDocument.getDrugId())
				.drugName(symptomDocument.getDrugName())
				.efficacy(symptomDocument.getEfficacy())
				.company(symptomDocument.getCompany())
				.imageUrl(symptomDocument.getImageUrl())
				.build();
	}


	/**
	 * Elasticsearch 응답을 Page<DrugSearchDomain> 형태로 변환합니다.
	 *
	 * @param resp    Elasticsearch 응답 객체
	 * @param request 검색 요청 정보
	 * @return 변환된 페이지 객체
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-05-04
	 */
	public static Page<DrugSearchDomain> toPageResponse(
		co.elastic.clients.elasticsearch.core.SearchResponse<DrugKeywordDocument> resp, SearchByKeywordParams request) {
		List<DrugSearchDomain> results = resp.hits().hits().stream()
			.map(Hit::source)
			.filter(Objects::nonNull)
			.map(DrugMapper::toDomainByDocument)
			.toList();

		long totalHits = Objects.requireNonNull(resp.hits().total()).value();
		return new PageImpl<>(results, PageRequest.of(request.getFrom(), request.getSize()), totalHits);
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

	/**
	 * Drug 도메인 객체를 DetailSearchResponse DTO로 변환합니다.
	 *
	 * @param d 변환할 Drug 객체
	 * @return DetailSearchResponse 응답 객체
	 *
	 * @author 함예정
	 * @since 2025-04-30
	 */
	public static DetailSearchResponse toDetailResponse(Drug d) {
		return DetailSearchResponse.builder()
			.drugId(d.getDrugId())
			.drugName(d.getDrugName())
			.company(d.getCompany())
			.efficacy(d.getEfficacy())
			.permitDate(d.getPermitDate())
			.isGeneral(d.isGeneral())
			.materialInfo(d.getMaterialInfo())
			.storeMethod(d.getStoreMethod())
			.validTerm(d.getValidTerm())
			.usage(d.getUsage())
			.precaution(d.getPrecaution())
			.imageUrl(d.getImageUrl())
			.cancelDate(d.getCancelDate())
			.cancelName(d.getCancelName())
			.isGeneral(d.isGeneral())
			.build();
	}

	/**
	 * GovDrugEntity 엔티티 객체를 Drug 도메인 객체로 변환합니다.
	 *
	 * @param d 변환할 GovDrugEntity 객체
	 * @return Drug 도메인 객체
	 *
	 * @author 함예정
	 * @since 2025-04-30
	 */
	public static Drug toDomainFromEntity(DrugEntity d) {
		return Drug.builder()
			.drugId(d.getId())
			.drugName(d.getDrugName())
			.company(d.getCompany())
			.efficacy(toListFromString(d.getEfficacy()))
			.permitDate(d.getPermitDate())
			.isGeneral(d.isGeneral())
			.materialInfo(toListFromString(d.getMaterialInfo()))
			.storeMethod(d.getStoreMethod())
			.validTerm(d.getValidTerm())
			.usage(toListFromString(d.getUsage()))
			.precaution(toMapFromString(d.getPrecaution()))
			.imageUrl(d.getImageUrl())
			.cancelDate(d.getCancelDate())
			.cancelName(d.getCancelName())
			.isGeneral(d.isGeneral())
			.build();
	}

	/**
	 * JSON 문자열을 List 객체로 파싱합니다.
	 *
	 * @param str JSON 형식의 문자열
	 * @return 파싱된 List 객체, 실패 시 null 반환
	 *
	 * @author 함예정
	 * @since 2025-04-30
	 */
	private static List toListFromString(String str){
		try {
			return objectMapper.readValue(str, List.class);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * JSON 문자열을 Map 객체로 파싱합니다.
	 *
	 * @param str JSON 형식의 문자열
	 * @return 파싱된 Map 객체, 실패 시 null 반환
	 *
	 * @author 함예정
	 * @since 2025-04-30
	 */
	private static Map toMapFromString(String str){
		try {
			return objectMapper.readValue(str, Map.class);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
