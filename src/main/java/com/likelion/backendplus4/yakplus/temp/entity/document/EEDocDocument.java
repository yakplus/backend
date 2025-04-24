package com.likelion.backendplus4.yakplus.temp.entity.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Document(indexName = "eedoc")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EEDocDocument {

	@Id
	@Field(type = FieldType.Keyword, name = "ITEM_SEQ")
	private String drugId;

	@Field(type = FieldType.Text, name = "ITEM_NAME")
	private String drugName;

	@Field(type = FieldType.Text, name = "symptom", analyzer = "only_nouns")
	private String symptom;

	// ↓ 프로퍼티명을 "symptomSuggester"로 변경해야 ES 매핑과 일치합니다
	@CompletionField(analyzer = "symptom_autocomplete")
	private List<String> symptomSuggester;
}
