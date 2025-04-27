package com.likelion.backendplus4.yakplus.search.domain.model;

import java.util.List;

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
    private List<String> efficacy;
    private String company;
    private String imageUrl;
}
