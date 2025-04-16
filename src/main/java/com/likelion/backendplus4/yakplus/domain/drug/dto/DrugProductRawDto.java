package com.likelion.backendplus4.yakplus.domain.drug.dto;

import java.util.List;

import com.likelion.backendplus4.yakplus.domain.drug.entity.Ingredient;

public record DrugProductRawDto(
	String itemSeq,           // 품목일련번호
	String itemName,          // 품목명
	String entpName,          // 업체명
	String itemPermitDate,    // 허가일자
	String etcOtcCode,        // 전문일반
	String chart,             // 성상
	List<Ingredient> ingredientList,      // 원료성분
	String storageMethod,     // 저장방법
	String validTerm,         // 유효기간
	String packUnit,          // 포장단위
	String makeMaterialFlag,  // 완제/원료구분
	String cancelName,        // 상태
	String totalContent,      // 총량
	String eeDocData,         // 효능효과 문서 데이터
	String udDocData,         // 용법용량 문서 데이터
	String nbDocData,         // 주의사항(일반) 문서 데이터
	String pnDocData,         // 주의사항(전문) 문서 데이터
	String ingrName,          // 첨가제
	String rareDrugYn         // 희귀의약품여부
) {}