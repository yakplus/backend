package com.likelion.backendplus4.yakplus.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.application.dto.in.DrugApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugApprovalDetailResponseDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugDetailDto;
import com.likelion.backendplus4.yakplus.domain.port.in.DrugApprovalDetailUseCase;
import com.likelion.backendplus4.yakplus.domain.port.out.DrugApprovalDetailPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugApprovalDetailService implements DrugApprovalDetailUseCase {
	private final DrugApprovalDetailPort drugApprovalDetailPort;
	@Override
	public DrugApprovalDetailResponseDto fetchDetails(DrugApprovalDetailRequestDto requestDto) {
		DrugApprovalDetailResponseDto response = drugApprovalDetailPort.fetchDrugDetails(requestDto);
		List<DrugDetailDto> detailList = response.getBody().getItems();
		for (DrugDetailDto detail : detailList) {
			System.out.println("▶ Drug Name: " + detail.getItemName());
			System.out.println("▶ Company Name: " + detail.getEntpName());
			System.out.println("▶ PermitDate: " + detail.getItemPermitDate());
			System.out.println("------------------------------------");
		}
		return response;
	}
}
