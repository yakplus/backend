package com.likelion.backendplus4.yakplus.drug.presentation.controller.dto;

import lombok.Builder;

@Builder
public record DrugSymptomResponse(
	Long drugId,
	String drugName,
	String symptom
) {
}
