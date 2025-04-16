package com.likelion.backendplus4.yakplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

/**
 * DrugCombinedInfo는 DrugPermitInfo와 DrugImageInfo의 정보를 결합한 도메인 객체이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Entity
@Table(name = "drug_combined_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugCombinedInfo {

    @Id
    @Column(name = "item_seq", nullable = false, length = 50)
    private String itemSeq;

    // DrugPermitInfo 필드
    @Column(name = "item_name", length = 255)
    private String itemName;
    @Column(name = "entp_name", length = 255)
    private String entpName;
    @Column(name = "item_permit_date")
    private LocalDate itemPermitDate;
    @Column(name = "etc_otc_code", length = 255)
    private String etcOtcCode;
    @Column(name = "chart", columnDefinition = "TEXT")
    private String chart;
    @Column(name = "material_name", columnDefinition = "TEXT")
    private String materialName;
    @Column(name = "storage_method", columnDefinition = "TEXT")
    private String storageMethod;
    @Column(name = "valid_term", length = 255)
    private String validTerm;
    @Column(name = "pack_unit", columnDefinition = "TEXT")
    private String packUnit;
    @Column(name = "make_material_flag", length = 255)
    private String makeMaterialFlag;
    @Column(name = "cancel_name", length = 255)
    private String cancelName;
    @Column(name = "total_content", columnDefinition = "TEXT")
    private String totalContent;
    @Column(name = "ee_doc_data", columnDefinition = "TEXT")
    private String eeDocData;
    @Column(name = "ud_doc_data", columnDefinition = "TEXT")
    private String udDocData;
    @Column(name = "nb_doc_data", columnDefinition = "TEXT")
    private String nbDocData;
    @Column(name = "pn_doc_data", columnDefinition = "TEXT")
    private String pnDocData;
    @Column(name = "ingr_name", columnDefinition = "TEXT")
    private String ingrName;
    @Column(name = "rare_drug_yn", length = 4)
    private String rareDrugYn;

    // DrugImageInfo 필드
    @Column(name = "big_prdt_img_url", columnDefinition = "TEXT")
    private String bigPrdtImgUrl;
}
