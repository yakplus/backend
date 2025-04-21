package com.likelion.backendplus4.yakplus.els.core;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {
    private Long itemSeq;
    private String itemName;
    private String entpName;
    private String eeText;
    // 필요시 추가 필드:
    private LocalDate itemPermitDate;
    private Map<String, Object> materialName;
    private String nbDocData;
    private String udDocData;
    private String storageMethod;
    private String validTerm;
    private String imgUrl;
}