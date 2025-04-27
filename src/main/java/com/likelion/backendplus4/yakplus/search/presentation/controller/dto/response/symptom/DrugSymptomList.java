package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.symptom;

import java.util.List;

public record DrugSymptomList(
	List<DrugSymptomResponse> symptomResponseList
) {
}
