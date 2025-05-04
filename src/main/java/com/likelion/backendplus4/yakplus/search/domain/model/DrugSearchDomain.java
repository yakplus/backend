package com.likelion.backendplus4.yakplus.search.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Elasticsearch 검색 결과를 담는 도메인 모델 클래스
 *
 * @since 2025-05-03
 */
@Getter
@AllArgsConstructor
@Builder
public class DrugSearchDomain {
    private Long drugId;
    private String drugName;
    private List<String> efficacy;
    private String company;
    private String imageUrl;
}
