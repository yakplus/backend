package com.likelion.backendplus4.yakplus.domain.port.in;

import com.likelion.backendplus4.yakplus.application.dto.in.MedicineApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.MedicineApprovalDetailResponseDto;

public interface MedicineApprovalDetailUseCase {
	MedicineApprovalDetailResponseDto fetchDetails(MedicineApprovalDetailRequestDto requestDto);

	int fetchAndSaveDetails(MedicineApprovalDetailRequestDto requestDto);
}
