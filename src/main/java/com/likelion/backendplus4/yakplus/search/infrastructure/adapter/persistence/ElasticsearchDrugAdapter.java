package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ElasticsearchDrugAdapter implements DrugIndexRepositoryPort, DrugSearchRepositoryPort {
    private static final String DRUGS_INDEX = "drugs_v2";
    private static final String SEARCH_INDEX = "drugs";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final EmbeddingPort embeddingPort;

    @Override
    public void saveAll(List<Drug> drugs) {
        drugs.forEach(this::saveDrug);
    }

    private void saveDrug(Drug drug) {
        try {
            float[] vector = embeddingPort.getEmbedding(drug.getEeText());

            Map<String, Object> source = createDrugDocument(drug, vector);
            String json = objectMapper.writeValueAsString(source);

            Request request = createIndexRequest(drug.getItemSeq(), json);
            restClient.performRequest(request);
        } catch (Exception ex) {
            throw new RuntimeException("ES 인덱싱 실패: itemSeq=" + drug.getItemSeq(), ex);
        }
    }

    private Map<String, Object> createDrugDocument(Drug drug, float[] vector) {
        return Map.of(
                "itemSeq", drug.getItemSeq().toString(),
                "itemName", drug.getItemName(),
                "entpName", drug.getEntpName(),
                "eeText", drug.getEeText(),
                "searchAll", drug.getEeText(),
                "eeVector", vector
        );
    }

    private Request createIndexRequest(Long itemSeq, String json) {
        Request request = new Request("POST", "/" + DRUGS_INDEX + "/_doc/" + itemSeq);
        request.setEntity(new NStringEntity(json, ContentType.APPLICATION_JSON));
        return request;
    }

    @Override
    public List<Drug> searchBySymptoms(String query, float[] vector, int size, int from) {
        try {
            String esQuery = buildSearchQuery(query, vector, size, from);
            Response response = executeSearch(esQuery);
            return parseSearchResults(response);
        } catch (Exception e) {
            throw new RuntimeException("ES 검색 실패", e);
        }
    }

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

    private Response executeSearch(String esQuery) throws IOException {
        Request request = new Request("GET", "/" + SEARCH_INDEX + "/_search");
        request.setEntity(new NStringEntity(esQuery, ContentType.APPLICATION_JSON));
        return restClient.performRequest(request);
    }

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