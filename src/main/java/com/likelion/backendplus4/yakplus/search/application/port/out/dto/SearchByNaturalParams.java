package com.likelion.backendplus4.yakplus.search.application.port.out.dto;

import lombok.Getter;

/**
 * 자연어 검색을 위한 Elasticsearch 파라미터를 담는 DTO 클래스입니다.
 *
 * query, vector, from, size 필드를 통해 검색 요청 정보를 정의합니다.
 *
 * @since 2025-05-03
 */
@Getter
public class SearchByNaturalParams {
    private final String query;
    private final float[] vector;
    private final int from;
    private final int size;

    /**
     * 주어진 검색어, 임베딩 벡터, 오프셋, 페이지 크기로 파라미터 객체를 생성합니다.
     *
     * @param query  검색어 문자열
     * @param vector 검색어 임베딩 벡터 (null일 수 없습니다)
     * @param from   조회 시작 오프셋 (0 이상)
     * @param size   페이지당 결과 개수 (1 이상)
     * @throws IllegalArgumentException vector가 null인 경우 발생
     * @author 정안식
     * @since 2025-05-03
     */
    public SearchByNaturalParams(String query, float[] vector, int from, int size) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector는 null일 수 없습니다.");
        }
        this.query = query;
        this.vector = vector;
        this.from = from;
        this.size = size;
    }
}
