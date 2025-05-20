package com.likelion.backendplus4.yakplus.search.infrastructure.support.natural;

import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;

/**
 * 자연어 검색 요청 정보를 도메인 모델로 변환하는 매퍼 클래스
 *
 * 이 매퍼는 컨트롤러에서 전달된 쿼리, 페이지 번호, 페이지 크기 정보를
 * DrugSearchNatural 객체로 조립하여 반환합니다.
 *
 * @since 2025-05-03
 */
public class SearchNaturalMapper {

    /**
     * 컨트롤러 파라미터로 전달된 쿼리, 페이지 번호, 페이지 크기를 기반으로
     * DrugSearchNatural 도메인 객체를 생성합니다.
     *
     * @param query 검색을 위한 자연어 쿼리 문자열
     * @param page  조회할 페이지 번호 (0부터 시작)
     * @param size  페이지당 결과 개수
     * @return 구성된 DrugSearchNatural 도메인 객체
     * @author 정안식
     * @since 2025-05-02
     */
    public static DrugSearchNatural toDrugSearchNatural(String query, int page, int size) {
        return DrugSearchNatural.builder()
                .query(query)
                .page(page)
                .size(size)
                .build();
    }
}
