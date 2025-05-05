package com.likelion.backendplus4.yakplus.search.infrastructure.support.natural;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Elasticsearch 응답의 JsonNode를 DrugSearchDomainNatural 도메인 모델로 변환하는 매퍼 클래스입니다.
 *
 * @since 2025-05-02
 */
public class DrugSearchNaturalMapper {
    /**
     * JsonNode의 필드 값을 읽어들여 DrugSearchDomainNatural 객체로 매핑합니다.
     *
     * @param src JsonNode 형태의 Elasticsearch 응답 _source 데이터
     * @return 매핑된 DrugSearchDomainNatural 도메인 객체
     * @author 정안식
     * @since 2025-05-03
     */
    public static DrugSearchDomainNatural toDomainNatural(JsonNode src) {
        return DrugSearchDomainNatural.builder()
                .drugId(src.path("drugId").asLong())
                .drugName(src.path("drugName").asText())
                .company(src.path("company").asText())
                .efficacy(StreamSupport.stream(src.path("efficacy").spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(Collectors.toList()))
                .imageUrl(src.path("imageUrl").asText(null))
                .build();
    }
}
