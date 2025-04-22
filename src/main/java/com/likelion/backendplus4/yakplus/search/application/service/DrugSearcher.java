package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
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
    public List<Drug> search(String query, int page, int size) {
        float[] embeddings = generateEmbeddings(query);
        return searchDrugs(query, embeddings, page, size);
    }

    private float[] generateEmbeddings(String query) {
        return embeddingPort.getEmbedding(query);
    }

    private List<Drug> searchDrugs(String query, float[] embeddings, int page, int size) {
        return drugSearchRepositoryPort.searchBySymptoms(query, embeddings, size, page * size);
    }
}