package com.likelion.backendplus4.yakplus.search.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DrugSearchDomainNatural {
    private Long drugId;
    private String drugName;
    private List<String> efficacy;
    private String company;
    private String imageUrl;
}
