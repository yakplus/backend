package com.likelion.backendplus4.yakplus.domain.drug.entity;

import com.likelion.backendplus4.yakplus.domain.drug.dto.DrugProductRawDto;
import com.likelion.backendplus4.yakplus.global.util.parser.XmlParserUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drug_product_raw")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugProductRaw {

	@Id
	@Column(name = "item_seq", nullable = false)
	private String itemSeq;  // 품목일련번호

	@Column(name = "item_name")
	private String itemName; // 품목명

	@Column(name = "entp_name")
	private String entpName; // 업체명

	@Column(name = "item_permit_date")
	private String itemPermitDate; // 허가일자

	@Column(name = "etc_otc_code")
	private String etcOtcCode; // 전문일반

	@Column(name = "chart")
	private String chart; // 성상

	@Column(name = "material_name")
	private String materialName; // 원료성분

	@Column(name = "storage_method")
	private String storageMethod; // 저장방법

	@Column(name = "valid_term")
	private String validTerm; // 유효기간

	@Column(name = "pack_unit")
	private String packUnit; // 포장단위

	@Column(name = "make_material_flag")
	private String makeMaterialFlag; // 완제/원료구분

	@Column(name = "cancel_name")
	private String cancelName; // 상태

	@Column(name = "total_content")
	private String totalContent; // 총량

	@Column(name = "ee_doc_data", columnDefinition = "TEXT")
	private String eeDocData; // 효능효과 문서 데이터

	@Column(name = "ud_doc_data", columnDefinition = "TEXT")
	private String udDocData; // 용법용량 문서 데이터

	@Column(name = "nb_doc_data", columnDefinition = "TEXT")
	private String nbDocData; // 주의사항(일반) 문서 데이터

	@Column(name = "pn_doc_data", columnDefinition = "TEXT")
	private String pnDocData; // 주의사항(전문) 문서 데이터

	@Column(name = "ingr_name")
	private String ingrName; // 첨가제

	@Column(name = "rare_drug_yn")
	private String rareDrugYn; // 희귀의약품여부


	/**
	 * DTO를 받아 DrugProductRaw 객체를 생성하는 팩토리 메서드
	 * XML 문서 데이터는 XmlParserUtil을 통해 텍스트만 추출합니다.
	 *
	 * @param dto 외부 API에서 받은 데이터 DTO (record 타입)
	 * @return DrugProductRaw 엔티티 객체
	 */
	public static DrugProductRaw from(DrugProductRawDto dto) {
		return DrugProductRaw.builder()
			.itemSeq(dto.itemSeq())
			.itemName(dto.itemName())
			.entpName(dto.entpName())
			.itemPermitDate(dto.itemPermitDate())
			.etcOtcCode(dto.etcOtcCode())
			.chart(dto.chart())
			.materialName(dto.materialName())
			.storageMethod(dto.storageMethod())
			.validTerm(dto.validTerm())
			.packUnit(dto.packUnit())
			.makeMaterialFlag(dto.makeMaterialFlag())
			.cancelName(dto.cancelName())
			.totalContent(dto.totalContent())
			// XML 문서 데이터는 유틸리티를 통해 텍스트만 추출하여 저장
			.eeDocData(XmlParserUtil.extractTextFromXml(dto.eeDocData()))
			.udDocData(XmlParserUtil.extractTextFromXml(dto.udDocData()))
			.nbDocData(XmlParserUtil.extractTextFromXml(dto.nbDocData()))
			.pnDocData(XmlParserUtil.extractTextFromXml(dto.pnDocData()))
			.ingrName(dto.ingrName())
			.rareDrugYn(dto.rareDrugYn())
			.build();
	}

}
