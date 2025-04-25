package com.likelion.backendplus4.yakplus.drug.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugSymptom {
    private Long drugId;
    private String drugName;
    private String symptom;
}
