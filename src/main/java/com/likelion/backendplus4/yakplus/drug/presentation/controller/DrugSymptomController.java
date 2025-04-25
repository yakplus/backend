package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugSymptomService;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomSearchListResponse;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/symptom")
public class DrugSymptomController {

	private final DrugSymptomService indexService;

	@PostMapping("/index")
	public ResponseEntity<ApiResponse<Void>> triggerIndex() {
		indexService.indexAll();
		return ApiResponse.success();
	}


	@GetMapping("/autocomplete")
	public ResponseEntity<ApiResponse<DrugSymptomSearchListResponse>> autocomplete(@RequestParam String q) throws IOException {
		DrugSymptomSearchListResponse results = indexService.getSymptomAutoComplete(q);
		return ApiResponse.success(results);
	}


}
