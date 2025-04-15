package com.likelion.backendplus4.yakplus.adapter.in.web;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.application.dto.in.DrugApprovalDetailRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug-approval-detail")
public class DrugApprovalDetailController {
	@PostMapping
	public ResponseEntity<?> fetchDrugDetails(@RequestBody String json, ObjectMapper mapper) throws
		JsonProcessingException {
		// post로 받은 json DrugApprovalDetailRequestDto에 매핑
		DrugApprovalDetailRequestDto requestDto = mapper.readValue(json, DrugApprovalDetailRequestDto.class);

		// 디버깅 용도: requestDto 값 출력
		System.out.println("Requested numOfRows: " + requestDto.getNumOfRows());

		// 추후 유스케이스 실행 예정
		// useCase.fetchDetails(requestDto);

		return ResponseEntity.ok().build();
	}
}
