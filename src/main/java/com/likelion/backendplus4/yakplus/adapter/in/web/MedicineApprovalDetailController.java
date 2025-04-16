package com.likelion.backendplus4.yakplus.adapter.in.web;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.application.dto.in.MedicineApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.domain.port.in.MedicineApprovalDetailUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug-approval-detail")
public class MedicineApprovalDetailController {

	private final MedicineApprovalDetailUseCase medicineApprovalDetailUseCase;
	@PostMapping
	public ResponseEntity<?> fetchDrugDetailsAndSave(@RequestBody String json, ObjectMapper mapper) throws
		JsonProcessingException {
		// post로 받은 json DrugApprovalDetailRequestDto에 매핑
		MedicineApprovalDetailRequestDto requestDto = mapper.readValue(json, MedicineApprovalDetailRequestDto.class);

		requestDto.setNumOfRows(10); // 입력 파라미터 안먹어서 row 직접 설정
		// 디버깅 용도: requestDto 값 출력
		System.out.println("Requested numOfRows: " + requestDto.getNumOfRows());

		// api 호출결과 리턴
		// MedicineApprovalDetailResponseDto response = medicineApprovalDetailUseCase.fetchDetails(requestDto);
		//  TODO: response => RDB에 들어갈 형태(DrugDetailEntity)로 가공
		int saveResult = medicineApprovalDetailUseCase.fetchAndSaveDetails(requestDto);



		return ResponseEntity.ok(saveResult);
	}
}
