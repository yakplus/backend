package com.likelion.backendplus4.yakplus.search.application.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import org.springframework.data.domain.Page;

import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;

public interface DrugSearchRepositoryPort {

    /**
     * 주어진 쿼리 및 임베딩 벡터를 사용해 Elasticsearch에서 검색을 수행하고,
     * 도메인 모델 리스트를 반환한다. 실패 시 SearchException을 발생시킨다.
     *
     * @param drugSearchNatural 검색어 및 페이지 정보를 담은 도메인 객체
     * @param embeddings        검색어에 대한 임베딩 벡터 배열
     * @return 검색된 Drug 도메인 객체 리스트
     * @throws SearchException 검색 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-05-03
     * 25.04.27 - 코드 리팩토링
     * 25.05.02 - 헥사고날 구조에 맞추어 코드 리팩토링
     * 25.05.03 - EsIndexName를 외부에 가져오도록 수정
     * @since 2025-04-22
     */
    List<DrugSearchDomainNatural> searchByNatural(DrugSearchNatural drugSearchNatural, float[] embeddings,String EsIndexName);

    /**
     * 사용자가 입력한 키워드를 바탕으로 Elasticsearch Completion Suggest API를 호출하여
     * symptomSuggester 필드에서 자동완성 추천 단어 리스트를 반환합니다.
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 키워드 리스트
     * @throws SearchException 자동완성 API 호출 실패 시 발생
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-28
     */
    List<String> getSymptomAutoCompleteResponse(String q);

    /**
     * 사용자가 입력한 키워드를 바탕으로 Elasticsearch Completion Suggest API를 호출하여
     * drugNameSuggester 필드에서 약품명 자동완성 추천 단어 리스트를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 키워드 리스트
     * @throws SearchException 자동완성 API 호출 실패 시 발생
     * @author 박찬병
     * @since 2025-04-28
     * @modified 2025-04-29
     */
    List<String> getDrugNameAutoCompleteResponse(String q);

    /**
     * 사용자가 입력한 키워드를 바탕으로 Elasticsearch Completion Suggest API를 호출하여
     * ingredientNameSuggester 필드에서 자동완성 추천 단어 리스트를 반환합니다.
     *
     * @param q 검색어 프리픽스
     * @return 자동완성 추천 성분명 리스트
     * @throws SearchException 자동완성 API 호출 실패 시 발생
     * @author 박찬병
     * @since 2025-05-01
     * @modified 2025-05-01
     */
    List<String> getIngredientAutoCompleteResponse(String q);

    /**
     * 검색어에 매칭되는 증상 문서 리스트를 Elasticsearch에서 조회합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 증상 문서 리스트 페이지 객체
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-05-03
     * @throws SearchException 검색 중 오류 발생 시
     */
    Page<DrugSearchDomain> searchDocsBySymptom(DrugSearchNatural request);

    /**
     * 검색어에 매칭되는 약품명 문서 리스트를 Elasticsearch에서 조회합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 약품명 문서 리스트 페이지 객체
     * @author 박찬병
     * @since 2025-04-28
     * @modified 2025-05-03
     * @throws SearchException 검색 중 오류 발생 시
     */
    Page<DrugSearchDomain> searchDocsByDrugName(DrugSearchNatural request);

    /**
     * 검색어에 매칭되는 성분명을 포함한 약품 문서 리스트를 Elasticsearch에서 조회합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 성분 기반 검색 결과 페이지 객체
     * @author 박찬병
     * @since 2025-05-01
     * @modified 2025-05-03
     * @throws SearchException 검색 중 오류 발생 시
     */
    Page<DrugSearchDomain> searchDocsByIngredient(DrugSearchNatural request);
}