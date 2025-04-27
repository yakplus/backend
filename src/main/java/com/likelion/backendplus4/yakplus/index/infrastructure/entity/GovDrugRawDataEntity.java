// package com.likelion.backendplus4.yakplus.index.infrastructure.entity;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.*;
//
// import java.time.LocalDate;
//
// @Entity
// @Table(name = "gov_drug_detail")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class GovDrugRawDataEntity {
//     @Id
//     private Long itemSeq;
//     private Boolean etcOtcCode;
//     private LocalDate itemPermitDate;
//     @Column(columnDefinition = "json")
//     private String eeDocData;
//     private String entpName;
//     private String itemName;
//     @Column(columnDefinition = "json")
//     private String materialName;
//     @Column(columnDefinition = "json")
//     private String nbDocData;
//     private String storageMethod;
//     @Column(columnDefinition = "json")
//     private String udDocData;
//     private String validTerm;
// //    private String imgUrl;
// }