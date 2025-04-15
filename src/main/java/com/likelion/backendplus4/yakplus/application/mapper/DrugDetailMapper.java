package com.likelion.backendplus4.yakplus.application.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugApprovalDetailResponseDto;
import com.likelion.backendplus4.yakplus.application.dto.out.external.DrugDetailDto;

public class DrugDetailMapper {
	public static DrugApprovalDetailResponseDto toResponseDto(Map<String, Object> raw) {
		Map<String, Object> headerMap = (Map<String, Object>) raw.get("header");
		Map<String, Object> bodyMap = (Map<String, Object>) raw.get("body");
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) bodyMap.get("items");

		DrugApprovalDetailResponseDto.Header header = new DrugApprovalDetailResponseDto.Header();
		header.setResultCode((String) headerMap.get("resultCode"));
		header.setResultMsg((String) headerMap.get("resultMsg"));

		DrugApprovalDetailResponseDto.Body body = new DrugApprovalDetailResponseDto.Body();
		body.setPageNo((Integer) bodyMap.get("pageNo"));
		body.setNumOfRows((Integer) bodyMap.get("numOfRows"));
		body.setTotalCount((Integer) bodyMap.get("totalCount"));
		List<DrugDetailDto> detailDtos = itemList.stream()
			.map(item -> {
				DrugDetailDto dto = new DrugDetailDto();
				dto.setItemSeq((String) item.get("ITEM_SEQ"));
				dto.setItemName((String) item.get("ITEM_NAME"));
				dto.setEntpName((String) item.get("ENTP_NAME"));
				dto.setItemPermitDate((String) item.get("ITEM_PERMIT_DATE"));
				dto.setEtcOtcCode((String) item.get("ETC_OTC_CODE"));
				dto.setChart((String) item.get("CHART"));
				dto.setMaterialName((String) item.get("MATERIAL_NAME"));
				dto.setStorageMethod((String) item.get("STORAGE_METHOD"));
				dto.setValidTerm((String) item.get("VALID_TERM"));
				dto.setPackUnit((String) item.get("PACK_UNIT"));
				dto.setMakeMaterialFlag((String) item.get("MAKE_MATERIAL_FLAG"));
				dto.setCancelName((String) item.get("CANCEL_NAME"));
				dto.setTotalContent((String) item.get("TOTAL_CONTENT"));
				dto.setEeDocData((String) item.get("EE_DOC_DATA"));
				dto.setUdDocData((String) item.get("UD_DOC_DATA"));
				dto.setNbDocData((String) item.get("NB_DOC_DATA"));
				dto.setPnDocData((String) item.get("PN_DOC_DATA"));
				dto.setIngrName((String) item.get("INGR_NAME"));
				dto.setRareDrugYn((String) item.get("RARE_DRUG_YN"));
				return dto;
			}).collect(Collectors.toList());

		body.setItems(detailDtos);

		DrugApprovalDetailResponseDto response = new DrugApprovalDetailResponseDto();
		response.setHeader(header);
		response.setBody(body);

		return response;
	}
}
