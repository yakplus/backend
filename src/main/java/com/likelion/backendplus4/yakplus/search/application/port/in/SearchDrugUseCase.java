package com.likelion.backendplus4.yakplus.search.application.port.in;

import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

import java.util.List;

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
     *
     * @author 함예정
     * @since 2025-04-30
     */
    DetailSearchResponse searchByDrugId(Long drugId);

    SearchResponseList searchDrugByNatural(DrugSearchNatural drugSearchNatural);

    AutoCompleteStringList getSymptomAutoComplete(String q);

    AutoCompleteStringList getDrugNameAutoComplete(String q);

    SearchResponseList searchDrugByDrugName(String q, int page, int size);

    SearchResponseList searchDrugBySymptom(String q, int page, int size);

}