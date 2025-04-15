package com.likelion.backendplus4.yakplus.domain.port.in;

import com.likelion.backendplus4.yakplus.application.dto.in.DrugApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugApprovalDetailResponseDto;

public interface DrugApprovalDetailUseCase {
	DrugApprovalDetailResponseDto fetchDetails(DrugApprovalDetailRequestDto requestDto);
}
