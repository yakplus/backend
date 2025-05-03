package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRdbRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.mapper.SearchByNaturalParamsMapper;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.DrugMapper;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.DetailSearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

import com.likelion.backendplus4.yakplus.search.presentation.mapper.SearchResponseMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 사용자 검색 요청을 처리하고, 벡터 유사도 및 텍스트 검색을 통해
 * 결과를 반환하는 서비스 구현체
 *
 * @modified 2025-05-02
 * @since 2025-04-22
 */
@Service
@RequiredArgsConstructor
public class DrugSearcher implements SearchDrugUseCase {
    private final DrugSearchRepositoryPort drugSearchRepositoryPort;
    private final EmbeddingPort embeddingPort;
    private final DrugSearchRdbRepositoryPort drugSearchRdbRepositoryPort;

    /**
     * 검색어 유효성 검사, 임베딩 생성, ES 검색 수행 후
     * 리스트로 매핑하여 반환한다.
     *
     * @param request 검색어 및 페이지 정보
     * @return 검색 결과 DTO 리스트
     * @throws SearchException 검색어가 유효하지 않은 경우 발생
     * @author 정안식
     * @modified 2025-05-02
     * - 25.05.02 - SearchResponseList를 반환하도록 수정
     * @since 2025-04-22
     */
    @Override
    public SearchResponseList searchDrugByNatural(DrugSearchNatural DrugSearchNatural) {
        log("search() 메서드 호출, 검색어: " + DrugSearchNatural.getQuery());
        return searchDrugs(DrugSearchNatural, generateEmbeddings(DrugSearchNatural.getQuery()));
    }

    /**
     * OpenAI API를 통해 검색어의 임베딩 벡터를 생성한다.
     *
     * @param query 검색어 문자열
     * @return 임베딩 벡터 배열
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    private float[] generateEmbeddings(String query) {
        log("generateEmbeddings() 메서드 호출, 검색어: " + query);
        return embeddingPort.getEmbedding(query);
    }

    /**
     * 생성된 임베딩 벡터와 검색 요청 정보를 이용해 Elasticsearch에서 조회를 수행하고,
     * 도메인 모델 리스트를 SearchResponse DTO 리스트로 변환해 반환한다.
     *
     * @param searchRequest 검색어 및 페이지/사이즈 정보가 담긴 DTO
     * @param embeddings    검색어에 대한 임베딩 벡터 배열
     * @return SearchResponse 객체 리스트
     * @author 정안식
     * @modified 2025-05-02
     * - 25.05.02 - SearchResponseList를 반환하도록 수정
     * @since 2025-04-22
     */
    private SearchResponseList searchDrugs(DrugSearchNatural drugSearchNatural, float[] embeddings) {
        log("searchDrugs() 메서드 호출");
        List<DrugSearchDomainNatural> drugs = drugSearchRepositoryPort.searchByNatural(SearchByNaturalParamsMapper.toParams(drugSearchNatural, embeddings));
        log("searchDrugs() 메서드 완료, 검색어: " + drugSearchNatural.getQuery() + ", 검색 결과 개수: " + drugs.size());
        return SearchResponseMapper.toResponseListWithCount(drugs);
    }

    /**
     * 의약품 ID를 통해 상세 정보를 조회합니다.
     *
     * @param drugId 조회할 의약품의 고유 ID
     * @return 변환된 상세 검색 응답 객체
     *
     * @author 함예정
     * @since 2025-04-30
     */
    @Override
    public DetailSearchResponse searchByDrugId(Long drugId){
        Drug drug = drugSearchRdbRepositoryPort.findById(drugId);
        return DrugMapper.toDetailResponse(drug);
    }

    /**
     * 주어진 사용자 입력 문자열을 바탕으로 증상 자동완성 키워드를 가져옵니다.
     * Elasticsearch에서 Suggest API 등을 활용하여 추천 결과를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-25
     */
    @Override
    public AutoCompleteStringList getSymptomAutoComplete(String q) {
        log("getSymptomAutoComplete() 메서드 호출, 검색어: " + q);
        return new AutoCompleteStringList(drugSearchRepositoryPort.getSymptomAutoCompleteResponse(q));
    }

    /**
     * 사용자 입력 문자열을 바탕으로 약품명 자동완성 추천 키워드를 조회합니다.
     *
     * Elasticsearch Suggest API를 활용하여 약품명과 관련된 자동완성 키워드 리스트를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO (AutoCompleteStringList)
     * @author 박찬병
     * @since 2025-04-29
     * @modified 2025-04-30
     */
    @Override
    public AutoCompleteStringList getDrugNameAutoComplete(String q) {
        log("getDrugNameAutoComplete() 메서드 호출, 검색어: " + q);
        return new AutoCompleteStringList(drugSearchRepositoryPort.getDrugNameAutoCompleteResponse(q));
    }

    /**
     * 주어진 증상 키워드로 검색하여 약품명 리스트를 반환합니다.
     *
     * @param q     검색어 프리픽스
     * @param page  조회할 페이지 번호
     * @param size  페이지 당 문서 수
     * @return 약품 리스트와 총 검색 결과 수를 포함하는 SearchResponseList DTO
     * @author 박찬병
     * @since 2025-04-25
     * @modified 2025-04-27
     */
    public SearchResponseList searchDrugBySymptom(String q, int page, int size) {
        log("searchDrugBySymptom() 메서드 호출, 검색어: " + q);
        Page<DrugSearchDomain> drugPage = drugSearchRepositoryPort.searchDocsBySymptom(q, page, size);
        return new SearchResponseList(
            drugPage.getContent().stream()
                .map(DrugMapper::toResponse)
                .toList(),
            drugPage.getTotalElements()
        );
    }

    /**
     * 주어진 약품명 키워드로 약품 리스트를 검색하여 반환합니다.
     *
     * @param q     검색어 프리픽스
     * @param page  조회할 페이지 번호
     * @param size  페이지 당 조회할 문서 수
     * @return 약품 리스트와 총 검색 결과 수를 포함하는 SearchResponseList DTO
     * @author 박찬병
     * @since 2025-04-29
     * @modified 2025-04-30
     */
    @Override
    public SearchResponseList searchDrugByDrugName(String q, int page, int size) {
        log("searchDrugByDrugName() 메서드 호출, 검색어: " + q);
        Page<DrugSearchDomain> drugPage = drugSearchRepositoryPort.searchDocsByDrugName(q, page, size);

        return new SearchResponseList(
            drugPage.getContent().stream()
                .map(DrugMapper::toResponse)
                .toList(),
            drugPage.getTotalElements()
        );
    }
}
