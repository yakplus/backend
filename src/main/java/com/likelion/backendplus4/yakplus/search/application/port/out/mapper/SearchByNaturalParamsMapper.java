package com.likelion.backendplus4.yakplus.search.application.port.out.mapper;

import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByNaturalParams;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;

/**
 * 자연어 검색 파라미터 객체로 변환하는 매퍼 클래스
 *
 * DrugSearchNatural 도메인 모델과 생성된 임베딩 배열을
 * SearchByNaturalParams DTO로 조립하여 반환합니다.
 *
 * @since 2025-05-02
 */
public class SearchByNaturalParamsMapper {

    /**
     * DrugSearchNatural과 임베딩 벡터를 기반으로
     * SearchByNaturalParams 객체를 생성합니다.
     *
     * @param natural    검색어, 페이지 정보가 담긴 도메인 모델
     * @param embeddings 검색어에 대한 임베딩 벡터 배열
     * @return 조립된 SearchByNaturalParams DTO
     * @author 정안식
     * @since 2025-05-02
     */
    public static SearchByNaturalParams toParams(DrugSearchNatural natural, float[] embeddings) {
        int from = natural.getPage() * natural.getSize();
        return new SearchByNaturalParams(natural.getQuery(), embeddings, from, natural.getSize());
    }
}
