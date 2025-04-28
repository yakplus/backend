package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 사용자 검색 요청을 처리하고, 벡터 유사도 및 텍스트 검색을 통해
 * 결과를 반환하는 서비스 구현체
 *
 * @modified 2025-04-24
 * @since 2025-04-22
 */
@Service
@RequiredArgsConstructor
public class DrugSearcher implements SearchDrugUseCase {
    private final DrugSearchRepositoryPort drugSearchRepositoryPort;
    private final EmbeddingPort embeddingPort;

    /**
     * 검색어 유효성 검사, 임베딩 생성, ES 검색 수행 후
     * 리스트로 매핑하여 반환한다.
     *
     * @param request 검색어 및 페이지 정보
     * @return 검색 결과 DTO 리스트
     * @throws SearchException 검색어가 유효하지 않은 경우 발생
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    @Override
    public List<SearchResponse> search(SearchRequest request) {
        log("search() 메서드 호출, 검색어: " + request.query());
        validateQuery(request.query());
        float[] embeddings = generateEmbeddings(request.query());
        return searchDrugs(request, embeddings);
    }

    /**
     * 검색어가 null이거나 빈 문자열인지 검사하고,
     * 유효하지 않으면 SearchException을 던진다.
     *
     * @param query 검색어 문자열
     * @throws SearchException INVALID_QUERY 코드와 함께 발생
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    private void validateQuery(String query) {
        log("validateQuery() 메서드 호출, 검색어: " + query);
        if (query == null || query.isEmpty()) {
            log(LogLevel.ERROR, "검색 쿼리가 비어있거나 null입니다.");
            throw new SearchException(SearchErrorCode.INVALID_QUERY);
        }
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
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    private List<SearchResponse> searchDrugs(SearchRequest searchRequest, float[] embeddings) {
        log("searchDrugs() 메서드 호출, 검색어: " + searchRequest.query());
        List<Drug> drugs = drugSearchRepositoryPort.searchBySymptoms(searchRequest.query(), embeddings, searchRequest.size(), searchRequest.page() * searchRequest.size());
        log("searchDrugs() 메서드 완료, 검색어: " + searchRequest.query() + ", 검색 결과 개수: " + drugs.size());
        return mapToDrugDomain(drugs);
    }

    /**
     * 도메인 모델 객체 리스트를 받아서, 각 객체의 필드를 추출한 후
     * SearchResponse DTO로 매핑하여 리스트로 반환한다.
     *
     * @param drugs 도메인 모델 Drug 객체 리스트
     * @return SearchResponse DTO 리스트
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-27
     * 25.04.27 - Drug 도메인 객체의 필드명에 맞추어 수정
     */
    private List<SearchResponse> mapToDrugDomain(List<Drug> drugs) {
        return drugs.stream()
                .map(d -> new SearchResponse(
                        d.getDrugId(),
                        d.getDrugName(),
                        d.getCompany(),
                        d.getEfficacy(),
                        d.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}