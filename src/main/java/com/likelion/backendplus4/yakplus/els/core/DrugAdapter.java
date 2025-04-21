package com.likelion.backendplus4.yakplus.els.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DrugAdapter implements DrugRepository {
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmbeddingDrugService  embedding;

//    public DrugAdapter(RestClient restClient) {
//        this.restClient = restClient;
//    }

    @Override
    public void saveAll(List<Drug> drugs) {
        for (Drug d : drugs) {
            try {
                float[] vector = embedding.getEmbedding(d.getEeText());  // 🔹 임베딩 추가

                Map<String,Object> src = Map.of(
                        "itemSeq",   d.getItemSeq().toString(),
                        "itemName",  d.getItemName(),
                        "entpName",  d.getEntpName(),
                        "eeText",    d.getEeText(),
                        "searchAll", d.getEeText(),
                        "eeVector",  vector                                     // 🔹 벡터 추가
                );
                String json = objectMapper.writeValueAsString(src);

                Request req = new Request("POST", "/drugs/_doc/" + d.getItemSeq());
                req.setEntity(new NStringEntity(json, ContentType.APPLICATION_JSON));
                restClient.performRequest(req);
            } catch (Exception ex) {
                throw new RuntimeException("ES indexing failed for itemSeq=" + d.getItemSeq(), ex);
            }
        }
    }

    @Override
    public List<Drug> searchBySymptoms(String query, float[] vector, int size, int from) {
        try {
            String vecJson = objectMapper.writeValueAsString(vector);
            String esQuery = """
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
            """.formatted(from, size, vecJson, query.replace("\"","\\\\\""));

            Request req = new Request("GET", "/drugs/_search");
            req.setEntity(new NStringEntity(esQuery, ContentType.APPLICATION_JSON));
            Response resp = restClient.performRequest(req);

            InputStream is = resp.getEntity().getContent();
            JsonNode hits = objectMapper.readTree(is)
                    .path("hits").path("hits");

            List<Drug> results = new ArrayList<>();
            for (JsonNode hit : hits) {
                JsonNode src = hit.path("_source");
                Drug d = Drug.builder()
                        .itemSeq(src.path("itemSeq").asLong())
                        .itemName(src.path("itemName").asText())
                        .entpName(src.path("entpName").asText())
                        .eeText(src.path("eeText").asText())
                        .build();
                results.add(d);
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException("ES search failed", ex);
        }
    }
}