package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchRequest;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugSearcher implements SearchDrugUseCase {
    private final DrugSearchRepositoryPort drugSearchRepositoryPort;
    private final EmbeddingPort embeddingPort;

    @Override
    public List<Drug> search(SearchRequest searchRequest) {
        float[] embeddings = generateEmbeddings(searchRequest.query());
        return searchDrugs(searchRequest, embeddings);
    }

    private float[] generateEmbeddings(String query) {
        return embeddingPort.getEmbedding(query);
    }

    private List<Drug> searchDrugs(SearchRequest searchRequest, float[] embeddings) {
        return drugSearchRepositoryPort.searchBySymptoms(searchRequest.query(), embeddings, searchRequest.size(), searchRequest.page() * searchRequest.size());
    }
}