package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSymptom;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.SymptomMapper;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
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

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;
import java.util.Objects;

/**
 * Elasticsearch를 통해 Drug 도메인 객체의 검색 기능을 제공하는 어댑터 클래스입니다.
 * DrugSearchRepositoryPort를 구현하여 Elasticsearch 원격 호출을 캡슐화합니다.
 *
 * @modified 2025-04-27
 * 25.04.27 - searchBySymptoms() 메서드 리팩토링
 * @since 2025-04-22
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
     * @modified 2025-04-27
     * 25.04.27 - 코드 리팩토링
     * @since 2025-04-22
     */
    @Override
    public List<Drug> searchBySymptoms(String query, float[] vector, int size, int from) {
        try {
            log("searchBySymptoms() 메서드 호출, 검색어: " + query);
            String esQuery = buildSearchQuery(query, vector, size, from);
            Response response = executeSearch(esQuery);
            return parseSearchResults(response);

        } catch (Exception e) {
            log(LogLevel.ERROR, "Elasticsearch 검색 실패: query = " + query, e);
            throw new SearchException(SearchErrorCode.ES_SEARCH_ERROR);
        }
    }

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
     * */
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
     * 검색어에 매칭되는 증상 문서 리스트를 Elasticsearch에서 조회합니다.
     *
     * @param q    검색어 프리픽스
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 페이지 당 문서 수
     * @return 증상 문서 리스트
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-28
     * @throws SearchException 검색 중 오류 발생 시
     */
    public List<DrugSymptom> searchDocsBySymptom(String q, int page, int size) {
        try {
            SearchResponse<DrugSymptomDocument> resp = esClient.search(s -> s
                    .index("eedoc")
                    .from(page * size)
                    .size(size)
                    .query(qb -> qb
                        .match(m -> m
                            .field("efficacy")   // only_nouns analyzer 적용된 필드
                            .query(q)           // 사용자가 입력한 q 값
                        )
                    )
                , DrugSymptomDocument.class);
            return resp.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(SymptomMapper::toDomain)
                .toList();
        } catch (IOException e) {
            throw new SearchException(SearchErrorCode.ES_SEARCH_FAIL);
        }
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
     * @modified 2025-04-27
     * 25.04.27 - 스크립트 필드명을 변경된 Drug 도메인 객체에 맞추어 수정
     * - 텍스트 매칭 필드 searchAll → efficacy 로 변경(현재는 약의 효과로만 검색하고 있음)
     * @since 2025-04-22
     */
    private String buildSearchQuery(String query, float[] vector, int size, int from) throws IOException {
        String vectorJson = objectMapper.writeValueAsString(vector);
        String escapedQuery = query.replace("\"", "\\\\\"");
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
                            "inline": "cosineSimilarity(params.queryVector, 'vector') + 1.0",
                            "lang": "painless",
                            "params": { "queryVector": %s }
                          }
                        }
                      },
                      "should": [
                        {
                          "match": {
                            "efficacy": {
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
                """.formatted(from, size, vectorJson, escapedQuery);
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
    private Response executeSearch(String esQuery) throws IOException {
        log("executeSearch() 메서드 호출");
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
     * @modified 2025-04-27
     * 25.04.27 - 스크립트 필드명을 변경된 Drug 도메인 객체에 맞추어 수정
     * @since 2025-04-22
     */
    private List<Drug> parseSearchResults(Response response) throws IOException {
        log("parseSearchResults() 메서드 호출");
        InputStream is = response.getEntity().getContent();
        JsonNode hits = objectMapper.readTree(is).path("hits").path("hits");

        List<Drug> results = new ArrayList<>();
        for (JsonNode hit : hits) {
            JsonNode src = hit.path("_source");

            long drugId = src.path("drugId").asLong();
            String drugName = src.path("drugName").asText();
            String company = src.path("company").asText();

            List<String> efficacyList = new ArrayList<>();
            for (JsonNode e : src.path("efficacy")) {
                efficacyList.add(e.asText());
            }

            String imageUrl = src.path("imageUrl").asText(null);
            float[] vectorArr = parseVector(src.path("vector"));

            Drug drug = Drug.builder()
                    .drugId(drugId)
                    .drugName(drugName)
                    .company(company)
                    .efficacy(efficacyList)
                    .imageUrl(imageUrl)
                    .vector(vectorArr)
                    .build();
            results.add(drug);
        }
        return results;
    }

    /**
     * JSON 배열로 전달된 vector 노드를 float[]로 변환한다.
     *
     * @param vectorNode vectors JSON 배열 노드
     * @return float[] 변환된 벡터 배열
     * @author 정안식
     * @since 2025-04-27
     */
    private float[] parseVector(JsonNode vectorNode) {
        if (!vectorNode.isArray()) {
            return new float[0];
        }
        float[] vec = new float[vectorNode.size()];
        for (int i = 0; i < vectorNode.size(); i++) {
            vec[i] = vectorNode.get(i).floatValue();
        }
        return vec;
    }
}