package com.likelion.backendplus4.yakplus.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.adapter.out.persistence.MedicineRepository;
import com.likelion.backendplus4.yakplus.application.dto.in.MedicineApprovalDetailRequestDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.MedicineApprovalDetailResponseDto;
import com.likelion.backendplus4.yakplus.application.dto.out.persistance.MedicineDetailDto;
import com.likelion.backendplus4.yakplus.application.mapper.MedicineDetailEntityMapper;
import com.likelion.backendplus4.yakplus.domain.entity.Medicine;
import com.likelion.backendplus4.yakplus.domain.port.in.MedicineApprovalDetailUseCase;
import com.likelion.backendplus4.yakplus.domain.port.out.MedicineApprovalDetailPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineApprovalDetailService implements MedicineApprovalDetailUseCase {
	private final MedicineApprovalDetailPort medicineApprovalDetailPort;
	private final MedicineRepository medicineRepository;

	@Override
	public MedicineApprovalDetailResponseDto fetchDetails(MedicineApprovalDetailRequestDto requestDto) {
		MedicineApprovalDetailResponseDto response = medicineApprovalDetailPort.fetchMedicineDetails(requestDto);
		List<MedicineDetailDto> detailList = response.getBody().getItems();
		for (MedicineDetailDto detail : detailList) {
			System.out.println("▶ Drug Name: " + detail.getItemName());
			System.out.println("▶ Company Name: " + detail.getEntpName());
			System.out.println("▶ PermitDate: " + detail.getItemPermitDate());
			System.out.println("------------------------------------");
		}
		return response;
	}

	@Override
	public int fetchAndSaveDetails(MedicineApprovalDetailRequestDto requestDto) {
		MedicineApprovalDetailResponseDto response = medicineApprovalDetailPort.fetchMedicineDetails(requestDto);
		List<MedicineDetailDto> detailList = response.getBody().getItems();
		if (detailList == null || detailList.isEmpty()) {
			return 0;
		}

		List<Medicine> entities = new ArrayList<>();

		for (MedicineDetailDto dto : detailList) {
			Medicine medicine = MedicineDetailEntityMapper.toEntity(dto);

			// 로그 찍기 (파싱 결과들 출력)
			System.out.println("itemSeq: " + medicine.getItemSeq());
			System.out.println("materialName: " + medicine.getMaterialName());
			System.out.println("itemPermitDate: " + medicine.getItemPermitDate());
			System.out.println("packUnit: " + medicine.getPackUnit());
			System.out.println("efficacy:\n" + medicine.getEfficacy());
			System.out.println("usage:\n" + medicine.getMedicationUsage());
			System.out.println("precaution:\n" + medicine.getPrecautionGeneral());
			System.out.println("====================================");

			entities.add(medicine);
		}



		// medicineRepository.saveAll(entities);
		return entities.size();
	}
}
