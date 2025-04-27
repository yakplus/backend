package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.symptom;

import lombok.Builder;

@Builder
public record DrugSymptomResponse(
	Long drugId,
	String drugName,
	String symptom
) {
}
