package com.likelion.backendplus4.yakplus.index.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Drug {
    private Long itemSeq;
    private String itemName;
    private String entpName;
    private String eeText;
    private LocalDate itemPermitDate;
    private Map<String, Object> materialName;
    private String nbDocData;
    private String udDocData;
    private String storageMethod;
    private String validTerm;
    private String imgUrl;
}