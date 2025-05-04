package com.likelion.backendplus4.yakplus.search.application.port.in;

import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

/**
 * 의약품 검색 관련 유스케이스를 정의하는 인터페이스입니다.
 * 다양한 조건에 따라 의약품을 검색하거나 자동완성 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
public interface SearchDrugUseCase {

    /**
     * 의약품 ID를 통해 상세 정보를 조회합니다.
     *
     * @param drugId 조회할 의약품의 고유 ID
     * @return 상세 검색 응답 객체
     * @author 함예정
     * @since 2025-04-30
     */
    DetailSearchResponse searchByDrugId(Long drugId);

    /**
     * 검색어 유효성 검사, 임베딩 생성, ES 검색 수행 후
     * 리스트로 매핑하여 반환한다.
     *
     * @param drugSearchNatural 검색어 및 페이지 정보를 담은 객체
     * @return 검색 결과 객체 리스트
     * @throws SearchException 검색어가 유효하지 않은 경우 발생
     * @author 정안식
     * @modified 2025-05-02
     * - 25.05.02 - SearchResponseList를 반환하도록 수정
     * @since 2025-04-22
     */
    SearchResponseList searchDrugByNatural(DrugSearchNatural drugSearchNatural);

    /**
     * 주어진 사용자 입력 문자열을 바탕으로 증상 자동완성 키워드를 가져옵니다.
     * Elasticsearch에서 Suggest API 등을 활용하여 추천 결과를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO
     * @author 박찬병
     * @modified 2025-04-25
     * @since 2025-04-24
     */
    AutoCompleteStringList getSymptomAutoComplete(String q);

    /**
     * 사용자 입력 문자열을 바탕으로 약품명 자동완성 추천 키워드를 조회합니다.
     * <p>
     * Elasticsearch Suggest API를 활용하여 약품명과 관련된 자동완성 키워드 리스트를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO (AutoCompleteStringList)
     * @author 박찬병
     * @modified 2025-04-30
     * @since 2025-04-29
     */
    AutoCompleteStringList getDrugNameAutoComplete(String q);

    /**
     * 사용자 입력 문자열을 바탕으로 성분명 자동완성 추천 키워드를 조회합니다.
     * <p>
     * Elasticsearch Suggest API를 활용하여 성분명과 관련된 자동완성 키워드 리스트를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO (AutoCompleteStringList)
     * @author 박찬병
     * @modified 2025-05-03
     * @since 2025-05-03
     */
    AutoCompleteStringList getIngredientAutoComplete(String q);

    /**
     * 주어진 증상 키워드로 검색하여 약품명 리스트를 반환합니다.
     *
     * @param drugSearch 검색어 및 페이지/사이즈 정보가 담긴 객체
     * @return 약품 리스트와 총 검색 결과 수를 포함하는 SearchResponseList DTO
     * @author 박찬병
     * @modified 2025-04-27
     * @since 2025-04-25
     */
    SearchResponseList searchDrugBySymptom(DrugSearchNatural drugSearch);


    /**
     * 주어진 약품명 키워드로 약품 리스트를 검색하여 반환합니다.
     *
     * @param drugSearch 검색어 및 페이지/사이즈 정보가 담긴 객체
     * @return 약품 리스트와 총 검색 결과 수를 포함하는 SearchResponseList DTO
     * @author 박찬병
     * @modified 2025-05-03
     * @since 2025-04-29
     */
    SearchResponseList searchDrugByDrugName(DrugSearchNatural drugSearch);

    /**
     * 주어진 성분명 키워드로 약품 리스트를 검색하여 반환합니다.
     *
     * @param drugSearch 검색어 및 페이지/사이즈 정보가 담긴 객체
     * @return 약품 리스트와 총 검색 결과 수를 포함하는 SearchResponseList DTO
     * @author 박찬병
     * @modified 2025-05-03
     * @since 2025-05-03
     */
    SearchResponseList searchDrugByIngredient(DrugSearchNatural drugSearch);

}