package com.likelion.backendplus4.yakplus.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicines")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Medicine {
	@Id
	@Column(name = "item_seq", nullable = false, length = 20)
	private String itemSeq; // 품목기준코드

	@Column(name = "item_name")
	private String itemName; // 품목명

	@Column(name = "entp_name")
	private String entpName; // 업체명

	@Column(name = "item_permit_date")
	private LocalDate itemPermitDate; // 허가일자

	@Column(name = "etc_otc_code")
	private String etcOtcCode; // 전문/일반 구분

	@Column(name = "chart", columnDefinition = "TEXT")
	private String chart; // 성상

	@Column(name = "material_name", columnDefinition = "TEXT")
	private String materialName; // 원료성분 => |단위로 끊어서 json 형태의 string으로 저장

	@Column(name = "storage_method", columnDefinition = "TEXT")
	private String storageMethod; // 저장방법

	@Column(name = "valid_term")
	private String validTerm; // 유효기간

	@Column(name = "pack_unit")
	private String packUnit; // 포장단위

	@Column(name = "make_material_flag")
	private String makeMaterialFlag; // 완제/원료 구분

	@Column(name = "cancel_name")
	private String cancelName; // 상태

	@Column(name = "total_content")
	private String totalContent; // 총량

	@Column(name = "efficacy", columnDefinition = "TEXT")
	private String efficacy; // 효능효과 문서 데이터 (EE_DOC_DATA)

	@Column(name = "medication_usage", columnDefinition = "TEXT")
	private String medicationUsage; // 용법용량 문서 데이터 (UD_DOC_DATA)

	@Column(name = "precaution_general", columnDefinition = "TEXT")
	private String precautionGeneral; // 주의사항(일반) 문서 데이터 (NB_DOC_DATA)

	@Column(name = "precaution_special", columnDefinition = "TEXT")
	private String precautionSpecial; // 주의사항(전문) 문서 데이터 (PN_DOC_DATA)

	@Column(name = "ingr_name", columnDefinition = "TEXT")
	private String ingrName; // 첨가제

	@Column(name = "rare_medicine_yn", length = 1)
	private String rareMedicineYn; // 희귀의약품여부 (Y/N)
}
