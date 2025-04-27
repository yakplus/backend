package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(indexName = "eedoc")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugSymptomDocument {

	@Id
	@Field(type = FieldType.Keyword, name = "ITEM_SEQ")
	@JsonProperty("ITEM_SEQ")
	private Long drugId;

	@Field(type = FieldType.Text, name = "ITEM_NAME")
	@JsonProperty("ITEM_NAME")
	private String drugName;

	@Field(type = FieldType.Text, name = "symptom", analyzer = "only_nouns")
	private String symptom;

	@CompletionField(analyzer = "symptom_autocomplete")
	private List<String> symptomSuggester;
}
