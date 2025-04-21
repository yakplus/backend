package com.likelion.backendplus4.yakplus.els.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "gov_drug_raw_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GovDrugRawData {
    @Id
    private Long itemSeq;
    private Boolean etcOtcCode;
    private java.time.LocalDate itemPermitDate;
    @Column(columnDefinition = "json")
    private String eeDocData;
    private String entpName;
    private String itemName;
    @Column(columnDefinition = "json")
    private String materialName;
    @Column(columnDefinition = "json")
    private String nbDocData;
    private String storageMethod;
    @Column(columnDefinition = "json")
    private String udDocData;
    private String validTerm;
    private String imgUrl;
}