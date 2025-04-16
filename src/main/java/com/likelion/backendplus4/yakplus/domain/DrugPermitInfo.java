package com.likelion.backendplus4.yakplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

/**
 * DrugPermitInfo는 의약품 허가정보를 나타내는 도메인 객체이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Entity
@Table(name = "drug_permit_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugPermitInfo {

    @Id
    @Column(name = "item_seq", nullable = false, length = 50)
    private String itemSeq;  // 품목기준코드

    @Column(name = "item_name", length = 255)
    private String itemName; // 품목명

    @Column(name = "entp_name", length = 255)
    private String entpName; // 업체명

    @Column(name = "item_permit_date")
    private LocalDate itemPermitDate; // 허가일자

    @Column(name = "etc_otc_code", length = 255)
    private String etcOtcCode; // 전문/일반 구분

    @Column(name = "chart", columnDefinition = "TEXT")
    private String chart; // 성상

    @Column(name = "material_name", columnDefinition = "TEXT")
    private String materialName; // 원료성분

    @Column(name = "storage_method", columnDefinition = "TEXT")
    private String storageMethod; // 저장방법

    @Column(name = "valid_term", length = 255)
    private String validTerm; // 유효기간

    @Column(name = "pack_unit", columnDefinition = "TEXT")
    private String packUnit; // 포장단위

    @Column(name = "make_material_flag", length = 255)
    private String makeMaterialFlag; // 완제/원료구분

    @Column(name = "cancel_name", length = 255)
    private String cancelName; // 취소여부, 상태

    @Column(name = "total_content", columnDefinition = "TEXT")
    private String totalContent; // 총량

    @Column(name = "ee_doc_data", columnDefinition = "TEXT")
    private String eeDocData; // 효능효과 문서 데이터

    @Column(name = "ud_doc_data", columnDefinition = "TEXT")
    private String udDocData; // 용법용량 문서 데이터

    @Column(name = "nb_doc_data", columnDefinition = "TEXT")
    private String nbDocData; // 주의사항(일반) 문서 데이터

    @Column(name = "pn_doc_data", columnDefinition = "TEXT")
    private String pnDocData; // 주의사항(전문) 문서 데이터

    @Column(name = "ingr_name", columnDefinition = "TEXT")
    private String ingrName; // 첨가제 또는 유효성분

    @Column(name = "rare_drug_yn", length = 4)
    private String rareDrugYn; // 희귀의약품 여부 (Y/N)
}
