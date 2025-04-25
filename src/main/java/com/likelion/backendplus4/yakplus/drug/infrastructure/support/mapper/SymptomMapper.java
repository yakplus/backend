package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import java.io.IOException;
import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugSymptom;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.JsonTextParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.SymptomTextParser;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomResponse;

/**
 * 증상 관련 Document를 다루는 매퍼 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-25
 * @modified 2025-04-25
 */
public class SymptomMapper {

	/**
	 * 주어진 GovDrug 도메인 객체를 기반으로 ES 색인용 DrugSymptomDocument로 변환합니다.
	 * 내부에서 JSON 파싱 및 전처리 로직을 실행하며, 파싱 실패 시 ScraperException을 던집니다.
	 *
	 * @param entity 변환 대상 GovDrug 도메인 객체
	 * @return 변환된 DrugSymptomDocument 객체
	 * @throws ScraperException JSON 파싱 실패 시 발생
	 * @author 박찬병
	 * @since 2025-04-25
	 * @modified 2025-04-25
	 */
	public static DrugSymptomDocument toDocument(GovDrug entity) {
		List<String> raws;
		try {
			// 1) JSON에서 "text"/"title" 필드의 모든 텍스트 추출
			raws = JsonTextParser.extractAllTexts(entity.getEfficacy());
		} catch (IOException e) {
			throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
		}

		// 2) 추출된 텍스트 리스트를 단일 문자열로 전처리
		String flatText = SymptomTextParser.flattenLines(raws);
		// 3) 전처리된 문자열을 자동완성용 토큰 리스트로 변환
		List<String> suggestTokens = SymptomTextParser.tokenizeForSuggestion(flatText);

		return DrugSymptomDocument.builder()
			.drugId(entity.getDrugId())
			.drugName(entity.getDrugName())
			.symptom(flatText)
			.symptomSuggester(suggestTokens)
			.build();
	}

	public static DrugSymptom toDomain(DrugSymptomDocument symptomDocument) {
		return DrugSymptom.builder()
			.drugId(symptomDocument.getDrugId())
			.drugName(symptomDocument.getDrugName())
			.symptom(symptomDocument.getSymptom())
			.build();
	}

	public static DrugSymptomResponse toResponse(DrugSymptom drugSymptom) {
		return DrugSymptomResponse.builder()
			.drugId(drugSymptom.getDrugId())
			.drugName(drugSymptom.getDrugName())
			.symptom(drugSymptom.getSymptom())
			.build();
	}
}
