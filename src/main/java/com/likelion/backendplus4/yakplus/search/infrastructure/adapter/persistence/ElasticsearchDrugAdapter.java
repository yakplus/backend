package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch를 통해 Drug 도메인 객체의 검색 기능을 제공하는 어댑터 클래스입니다.
 * DrugSearchRepositoryPort를 구현하여
 * Elasticsearch 원격 호출을 캡슐화합니다.
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
@Component
@RequiredArgsConstructor
public class ElasticsearchDrugAdapter implements DrugSearchRepositoryPort {
    private static final String SEARCH_INDEX = "drugs";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ElasticsearchClient esClient;

    /**
     * 주어진 쿼리 및 임베딩 벡터를 사용해 Elasticsearch에서 검색을 수행하고,
     * 도메인 모델 리스트를 반환한다. 실패 시 SearchException을 발생시킨다.
     *
     * @param query  사용자 검색어
     * @param vector 검색어 임베딩 벡터
     * @param size   한 페이지에 조회할 문서 수
     * @param from   조회 시작 오프셋
     * @return 검색된 Drug 도메인 객체 리스트
     * @throws SearchException 검색 처리 중 오류 발생 시
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    @Override
    public List<Drug> searchBySymptoms(String query, float[] vector, int size, int from) {
        try {
            String esQuery = buildSearchQuery(query, vector, size, from);
            Response response = executeSearch(esQuery);
            List<Drug> results = parseSearchResults(response);
            return results;

        } catch (Exception e) {
            //TODO: LOG ERROR 처리 요망
//            log(LogLevel.ERROR, "Elasticsearch 검색 실패: query = " + query, e);
            throw new SearchException(SearchErrorCode.ES_SEARCH_ERROR);
        }
    }

    @Override
    public List<String> getSymptomAutoCompleteResponse(String q) {
        SearchResponse<Void> resp;
        try {
            resp = esClient.search(s -> s
                    .index("eedoc")
                    .suggest(su -> su
                        .suggesters("symp_sugg", sg -> sg
                            .prefix(q)
                            .completion(c -> c
                                .field("symptomSuggester")
                                .analyzer("symptom_search_autocomplete")  // ← 이 줄만 추가
                                .size(20)
                            )
                        )
                    )
                , Void.class);
        } catch (IOException e) {
            throw new SearchException(SearchErrorCode.ES_SUGGEST_SEARCH_FAIL);
        }

        // Suggest 파싱
        return resp.suggest().get("symp_sugg")
            .get(0).completion().options().stream()
            .map(CompletionSuggestOption::text)
            .distinct()
            .toList();
    }

    /**
     * 검색 파라미터를 바탕으로 Elasticsearch Query DSL을 문자열로 조합한다.
     * 벡터 유사도와 텍스트 매칭을 결합한 복합 쿼리를 생성한다.
     *
     * @param query  검색어
     * @param vector 검색어 임베딩 벡터
     * @param size   한 페이지에 조회할 문서 수
     * @param from   조회 시작 오프셋
     * @return Elasticsearch에 전달할 쿼리 JSON 문자열
     * @throws IOException JSON 직렬화 과정에서 오류 발생 시
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private String buildSearchQuery(String query, float[] vector, int size, int from) throws IOException {
        String vectorJson = objectMapper.writeValueAsString(vector);
        return """
                {
                  "from": %d,
                  "size": %d,
                  "query": {
                    "bool": {
                      "must": {
                        "script_score": {
                          "query": { "match_all": {} },
                          "script": {
                            "inline": "cosineSimilarity(params.queryVector, 'eeVector') + 1.0",
                            "lang": "painless",
                            "params": { "queryVector": %s }
                          }
                        }
                      },
                      "should": [
                        {
                          "match": {
                            "searchAll": {
                              "query": "%s",
                              "fuzziness": "AUTO",
                              "boost": 0.2
                            }
                          }
                        }
                      ]
                    }
                  }
                }
                """.formatted(from, size, vectorJson, query.replace("\"", "\\\\\""));
    }

    /**
     * Elasticsearch에 빌드된 쿼리를 전송하여 검색 결과 Response를 반환한다.
     *
     * @param esQuery Elasticsearch Query DSL JSON 문자열
     * @return Elasticsearch 응답 객체
     * @throws IOException 요청 전송 또는 응답 처리 중 오류 발생 시
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private Response executeSearch(String esQuery) throws IOException {
        Request request = new Request("GET", "/" + SEARCH_INDEX + "/_search");
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
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private List<Drug> parseSearchResults(Response response) throws IOException {
        InputStream is = response.getEntity().getContent();
        JsonNode hits = objectMapper.readTree(is).path("hits").path("hits");

        List<Drug> results = new ArrayList<>();
        for (JsonNode hit : hits) {
            JsonNode source = hit.path("_source");
            Drug drug = Drug.builder()
                    .itemSeq(source.path("itemSeq").asLong())
                    .itemName(source.path("itemName").asText())
                    .entpName(source.path("entpName").asText())
                    .eeText(source.path("eeText").asText())
                    .build();
            results.add(drug);
        }
        return results;
    }
}