package com.likelion.backendplus4.yakplus.adapter.in.web;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.application.dto.in.DrugApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugApprovalDetailResponseDto;
import com.likelion.backendplus4.yakplus.domain.port.in.DrugApprovalDetailUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug-approval-detail")
public class DrugApprovalDetailController {

	private final DrugApprovalDetailUseCase drugApprovalDetailUseCase;
	@PostMapping
	public ResponseEntity<?> fetchDrugDetails(@RequestBody String json, ObjectMapper mapper) throws
		JsonProcessingException {
		// post로 받은 json DrugApprovalDetailRequestDto에 매핑
		DrugApprovalDetailRequestDto requestDto = mapper.readValue(json, DrugApprovalDetailRequestDto.class);

		// 디버깅 용도: requestDto 값 출력
		System.out.println("Requested numOfRows: " + requestDto.getNumOfRows());

		//
		DrugApprovalDetailResponseDto response = drugApprovalDetailUseCase.fetchDetails(requestDto);
		System.out.println("Request header check: " + response.getHeader());

		return ResponseEntity.ok(response);
	}
}
