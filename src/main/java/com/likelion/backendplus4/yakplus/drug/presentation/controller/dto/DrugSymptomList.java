package com.likelion.backendplus4.yakplus.drug.presentation.controller.dto;

import java.util.List;

public record DrugSymptomList(
	List<DrugSymptomResponse> symptomResponseList
) {
}
