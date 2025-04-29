package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(indexName = "drug_name")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugNameDocument {

	/** DB 상 고유 식별자 */
	@Id
	@Field(type = FieldType.Keyword, name = "drugId")
	private Long drugId;

	/** 일반 검색용 약품명 (풀텍스트 + .keyword 서브필드) */
	@Field(type = FieldType.Text, name = "drugName")
	private String drugName;

	/** (선택) 회사 정보 */
	@Field(type = FieldType.Keyword, name = "company")
	private String company;

	@Field(type = FieldType.Text, name = "efficacy")
	private List<String> efficacy;

	/** (선택) 이미지 URL */
	@Field(type = FieldType.Keyword, name = "imageUrl")
	private String imageUrl;

	/**
	 * 자동완성 전용 필드
	 * toDocument 변환 시: List.of(drugName) 식으로 세팅
	 */
	@CompletionField(
		analyzer = "drugName_autocomplete",
		searchAnalyzer = "drugName_autocomplete"
	)
	private List<String> drugNameSuggester;
}