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

	@Id
	@Field(type = FieldType.Keyword, name = "drugId")
	private Long drugId;

	@Field(type = FieldType.Text, name = "drugName")
	private String drugName;

	@Field(type = FieldType.Keyword, name = "company")
	private String company;

	@Field(type = FieldType.Text, name = "efficacy")
	private List<String> efficacy;

	@Field(type = FieldType.Keyword, name = "imageUrl")
	private String imageUrl;


	@CompletionField(
		analyzer = "drugName_autocomplete",
		searchAnalyzer = "drugName_autocomplete"
	)
	private List<String> drugNameSuggester;
}