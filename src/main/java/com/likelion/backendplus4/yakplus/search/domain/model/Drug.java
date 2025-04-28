package com.likelion.backendplus4.yakplus.search.domain.model;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {
    private Long drugId;
    private String drugName;
    private String company;
    private List<String> efficacy;
    private float[] vector;
    private LocalDate permitDate;
    private boolean isGeneral;
    private List<Material> materialInfo;
    private String storeMethod;
    private String validTerm;
    private List<String> usage;
    private Map<String, List<String>> precaution;
    private String imageUrl;
}