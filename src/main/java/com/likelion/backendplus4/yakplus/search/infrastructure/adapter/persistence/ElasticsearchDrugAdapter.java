package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByKeywordParams;
import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByNaturalParams;
import com.likelion.backendplus4.yakplus.search.application.port.out.mapper.SearchByKeywordParamsMapper;
import com.likelion.backendplus4.yakplus.search.application.port.out.mapper.SearchByNaturalParamsMapper;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugKeywordDocument;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.DrugMapper;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.natural.DrugSearchNaturalMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * Elasticsearch를 통해 Drug 도메인 객체의 검색 기능을 제공하는 어댑터 클래스입니다.
 * DrugSearchRepositoryPort를 구현하여 Elasticsearch 원격 호출을 캡슐화합니다.
 *
 * @modified 2025-05-06
 * 25.04.27 - searchBySymptoms() 메서드 리팩토링
 * 25.04.29 - 약품명 검색 기능 추가
 * @since 2025-04-22
 */
@Component
@RequiredArgsConstructor
public class ElasticsearchDrugAdapter implements DrugSearchRepositoryPort {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ElasticsearchClient esClient;

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
    @Override
    public List<DrugSearchDomainNatural> searchByNatural(DrugSearchNatural drugSearchNatural, float[] embeddings, String EsIndexName) {
        SearchByNaturalParams params = SearchByNaturalParamsMapper.toParams(drugSearchNatural, embeddings);
        log("searchBySymptoms() 메서드 호출, 검색어: " + params.getQuery());
        try {
            String esQuery = buildSearchQuery(
                    params.getVector(),
                    params.getSize(),
                    params.getFrom()
            );
            Response response = executeSearch(esQuery, EsIndexName);
            return parseSearchResults(response);

        } catch (Exception e) {
            log(LogLevel.ERROR, "Elasticsearch 검색 실패: query = " + params.getQuery(), e);
            throw new SearchException(SearchErrorCode.ES_SEARCH_ERROR);
        }
    }

    /**
     * 증상 자동완성 추천 결과를 조회합니다.
     * <p>
     * Elasticsearch의 Completion Suggest API를 활용하여 symptomSuggester 필드에서
     * 증상 키워드 자동완성 리스트를 반환합니다.
     *
     * @param q 검색어 프리픽스
     * @return 추천된 증상 문자열 리스트
     * @throws SearchException 검색 실패 시 예외 발생
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-24
     */
    @Override
    public List<String> getSymptomAutoCompleteResponse(String q) {
        return getAutoCompleteResponse("symptom_dictionary", "symp_sugg", "symptomSuggester", "symptom_autocomplete", q);
    }

    /**
     * 약품명 자동완성 추천 결과를 조회합니다.
     * <p>
     * drugNameSuggester 필드에서 Completion Suggest API를 통해 약품명 프리픽스 기반
     * 추천 문자열 리스트를 반환합니다.
     *
     * @param q 사용자 입력 프리픽스
     * @return 추천된 약품명 리스트
     * @throws SearchException 검색 실패 시 예외 발생
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-28
     */
    @Override
    public List<String> getDrugNameAutoCompleteResponse(String q) {
        return getAutoCompleteResponse("drug_keyword", "name_sugg", "drugNameSuggester", "drugName_autocomplete", q);
    }

    /**
     * 성분명 자동완성 추천 결과를 조회합니다.
     * <p>
     * ingredientNameSuggester 필드를 기반으로 사용자의 입력값에 대한 자동완성
     * 추천 리스트를 반환합니다.
     *
     * @param q 검색어 프리픽스
     * @return 추천된 성분명 리스트
     * @throws SearchException 검색 실패 시 예외 발생
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-05-01
     */
    @Override
    public List<String> getIngredientAutoCompleteResponse(String q) {
        return getAutoCompleteResponse("drug_keyword", "ingr_sugg", "ingredientNameSuggester", "drugName_autocomplete", q);
    }

    /**
     * 검색어에 매칭되는 증상 문서 리스트를 Elasticsearch에서 조회합니다.
     * <p>
     * match 쿼리를 사용하여 efficacy_list 필드에서 검색을 수행합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 증상 문서 리스트 페이지 객체
     * @throws SearchException 검색 중 오류 발생 시
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-24
     */
    @Override
    public Page<DrugSearchDomain> searchDocsBySymptom(DrugSearchNatural request) {
        SearchByKeywordParams params = SearchByKeywordParamsMapper.toParams(request);
        return searchWithMatch(params, "efficacy_list");
    }

    /**
     * 검색어에 매칭되는 약품명 문서 리스트를 Elasticsearch에서 조회합니다.
     * <p>
     * match 쿼리를 사용하여 drugName 필드에서 검색을 수행합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 약품명 문서 리스트 페이지 객체
     * @throws SearchException 검색 중 오류 발생 시
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-28
     */
    @Override
    public Page<DrugSearchDomain> searchDocsByDrugName(DrugSearchNatural request) {
        SearchByKeywordParams params = SearchByKeywordParamsMapper.toParams(request);
        return searchWithMatchPrefix(params, "drugName");
    }

    /**
     * 검색어에 매칭되는 성분명 문서 리스트를 Elasticsearch에서 조회합니다.
     * <p>
     * match 쿼리를 사용하여 ingredientName 필드에서 검색을 수행합니다.
     *
     * @param request 사용자가 입력한 자연어 검색 요청 DTO
     * @return 성분 기반 검색 결과 페이지 객체
     * @throws SearchException 검색 중 오류 발생 시
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-05-01
     */
    @Override
    public Page<DrugSearchDomain> searchDocsByIngredient(DrugSearchNatural request) {
        SearchByKeywordParams params = SearchByKeywordParamsMapper.toParams(request);
        return searchWithMatch(params, "ingredientName");
    }


    /**
     * 검색 파라미터를 바탕으로 Elasticsearch Query DSL을 JSON 문자열로 조합합니다.
     *
     * @param vector 검색어 임베딩 벡터
     * @param size   한 페이지에 조회할 문서 수
     * @param from   조회 시작 오프셋
     * @return Elasticsearch에 전달할 쿼리 JSON 문자열
     * @throws IOException JSON 직렬화 과정에서 오류 발생 시
     * @author 정안식
     * @modified 2025-05-06
     * 25.04.27 - 스크립트 필드명을 변경된 Drug 도메인 객체에 맞추어 수정
     * - 텍스트 매칭 필드 searchAll → efficacy 로 변경(현재는 약의 효과로만 검색하고 있음)
     * 25.05.06 - 벡터 유사도를 기준으로 필터링 및 보너스 계산 로직 적용
     * - 텍스트 매칭 필드에 대한 부스트 제거 및 벡터 유사도 기반으로만 순위 정렬
     * - 유사도 기준에 따라 문서의 점수 계산 (cutoff 및 bonus 적용)
     * @since 2025-04-22
     */
    private String buildSearchQuery(float[] vector, int size, int from) throws IOException {
        String vectorJson = objectMapper.writeValueAsString(vector);

        String painless =
                "double v = cosineSimilarity(params.q, 'vector') + 1.0; " +
                        "if (v < params.cutoff) return 0.0; " +
                        "double bonus = v >= params.cutoff + 0.15 ? v * params.boostFactor : 0.0; " +
                        "return v + bonus;";

        return String.format("""
                {
                  "from": %d,
                  "size": %d,
                  "sort": [
                    { "_score": { "order": "desc" } },
                    { "drugId": { "order": "asc" } }
                  ],
                  "query": {
                    "script_score": {
                      "query": { "match_all": {} },
                      "script": {
                        "source": "%s",
                        "lang": "painless",
                        "params": {
                          "q":          %s,
                          "cutoff":     1.15,
                          "boostFactor": 0.5
                        }
                      }
                    }
                  }
                }
                """, from, size, painless.replace("\"", "\\\\\""), vectorJson
        );
    }

    /**
     * Elasticsearch에 빌드된 쿼리를 전송하여 검색 결과 Response를 반환한다.
     *
     * @param esQuery Elasticsearch Query DSL JSON 문자열
     * @return Elasticsearch 응답 객체
     * @throws IOException 요청 전송 또는 응답 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    private Response executeSearch(String esQuery, String EsIndexName) throws IOException {
        log("executeSearch() 메서드 호출");
        Request request = new Request("GET", "/" + EsIndexName + "/_search");
        request.setEntity(new NStringEntity(esQuery, ContentType.APPLICATION_JSON));
        return restClient.performRequest(request);
    }

    /**
     * Elasticsearch 검색 결과 Response에서 hits 배열을 파싱해
     * Drug 도메인 객체 리스트로 변환한다.
     *
     * @param response Elasticsearch 검색 응답 객체
     * @return 파싱된 Drug 도메인 객체 리스트
     * @throws IOException 응답 스트림 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-05-02
     * 25.04.27 - 스크립트 필드명을 변경된 Drug 도메인 객체에 맞추어 수정
     * 25.05.02 - 기존 로직(단순 순환 및 List 매칭) StreamSupport을 사용하도록 개선
     * @since 2025-05-03
     */
    private List<DrugSearchDomainNatural> parseSearchResults(Response response) throws IOException {
        log("parseSearchResults() 메서드 호출");
        try (InputStream is = response.getEntity().getContent()) {
            JsonNode hits = objectMapper.readTree(is)
                    .path("hits")
                    .path("hits");
            return StreamSupport.stream(hits.spliterator(), false)
                    .map(hit -> hit.path("_source"))
                    .map(DrugSearchNaturalMapper::toDomainNatural)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Completion Suggest API를 사용하여 자동완성 추천 결과를 조회합니다.
     * <p>
     * 사용자가 입력한 프리픽스(query)를 바탕으로 Elasticsearch Suggest API를 호출하고,
     * 지정된 필드에서 자동완성 후보 리스트를 추출합니다.
     *
     * @param indexName    검색 대상 인덱스 이름
     * @param suggesterKey suggest 응답에서 사용할 key 이름
     * @param fieldName    자동완성 필드명
     * @param analyzer     사용할 분석기(analyzer) 이름
     * @param q            검색어 프리픽스 (사용자 입력값)
     * @return 자동완성 추천 문자열 리스트
     * @throws SearchException Elasticsearch 통신 또는 파싱 실패 시 예외 발생
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-05-04
     */
    private List<String> getAutoCompleteResponse(String indexName, String suggesterKey, String fieldName, String analyzer, String q) {
        log("getAutoCompleteResponse() 호출 - index: " + indexName + ", field: " + fieldName + ", query: " + q);

        try {
            SearchResponse<Void> resp = esClient.search(s -> s
                            .index(indexName)
                            .suggest(su -> su
                                    .suggesters(suggesterKey, sg -> sg
                                            .prefix(q)
                                            .completion(c -> c
                                                    .field(fieldName)
                                                    .analyzer(analyzer)
                                                    .size(10)
                                            )
                                    )
                            ),
                    Void.class
            );

            return resp.suggest()
                    .get(suggesterKey)
                    .getFirst()
                    .completion()
                    .options()
                    .stream()
                    .map(CompletionSuggestOption::text)
                    .distinct()
                    .toList();

        } catch (IOException e) {
            log(LogLevel.ERROR, "Elasticsearch 자동완성 실패: query = " + q, e);
            throw new SearchException(SearchErrorCode.ES_SUGGEST_SEARCH_FAIL);
        }
    }

    /**
     * match 쿼리를 사용하는 Elasticsearch 검색 메서드입니다.
     *
     * @param request   검색 요청 정보 (쿼리, 페이지, 사이즈)
     * @param fieldName 검색 대상 필드명
     * @return 검색 결과 페이지 객체
     * @throws SearchException 검색 중 Elasticsearch 예외 발생 시
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-24
     */
    private Page<DrugSearchDomain> searchWithMatch(SearchByKeywordParams request, String fieldName) {
        log("searchWithMatch() 호출 - field: " + fieldName + ", query: " + request.getQuery());
        try {
            SearchResponse<DrugKeywordDocument> resp = esClient.search(s -> s
                            .index("drug_keyword")
                            .from(request.getFrom() * request.getSize())
                            .size(request.getSize())
                            .query(qb -> qb
                                    .match(m -> m
                                            .field(fieldName)
                                            .query(request.getQuery())
                                    )
                            ),
                    DrugKeywordDocument.class);

            return toPageResponse(resp, request);

        } catch (IOException e) {
            log(LogLevel.ERROR, "Elasticsearch 검색 실패: query = " + request.getQuery(), e);
            throw new SearchException(SearchErrorCode.ES_SEARCH_FAIL);
        }
    }

    /**
     * match_phrase_prefix 쿼리를 사용하는 Elasticsearch 검색 메서드입니다.
     *
     * @param request   검색 요청 정보 (쿼리, 페이지, 사이즈)
     * @param fieldName 검색 대상 필드명
     * @return 검색 결과 페이지 객체
     * @throws SearchException 검색 중 Elasticsearch 예외 발생 시
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-24
     */
    private Page<DrugSearchDomain> searchWithMatchPrefix(SearchByKeywordParams request, String fieldName) {
        log("searchWithMatchPrefix() 호출 - field: " + fieldName + ", query: " + request.getQuery());
        try {
            SearchResponse<DrugKeywordDocument> resp = esClient.search(s -> s
                            .index("drug_keyword")
                            .from(request.getFrom() * request.getSize())
                            .size(request.getSize())
                            .query(qb -> qb
                                    .matchPhrasePrefix(mpp -> mpp
                                            .field(fieldName)
                                            .query(request.getQuery())
                                    )
                            ),
                    DrugKeywordDocument.class);

            return toPageResponse(resp, request);

        } catch (IOException e) {
            log(LogLevel.ERROR, "Elasticsearch 검색 실패: query = " + request.getQuery(), e);
            throw new SearchException(SearchErrorCode.ES_SEARCH_FAIL);
        }
    }

    /**
     * Elasticsearch 응답을 Page<DrugSearchDomain> 형태로 변환합니다.
     *
     * @param resp    Elasticsearch 응답 객체
     * @param request 검색 요청 정보
     * @return 변환된 페이지 객체
     * @author 박찬병
     * @modified 2025-05-04
     * @since 2025-04-24
     */
    private Page<DrugSearchDomain> toPageResponse(SearchResponse<DrugKeywordDocument> resp, SearchByKeywordParams request) {
        List<DrugSearchDomain> results = resp.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(DrugMapper::toDomainByDocument)
                .toList();

        long totalHits = resp.hits().total().value();
        return new PageImpl<>(results, PageRequest.of(request.getFrom(), request.getSize()), totalHits);
    }

}