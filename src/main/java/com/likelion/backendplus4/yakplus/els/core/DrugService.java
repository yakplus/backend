package com.likelion.backendplus4.yakplus.els.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class DrugService {
    private final GovDrugRawDataRepository rawRepo;
    private final DrugRepository        drugRepo;
    private final EmbeddingDrugService  embedding;

    public void indexAll() {
        List<GovDrugRawData> raws = rawRepo.findTop100ByOrderByItemSeqAsc();
        List<Drug> domains = raws.stream().map(e ->
                Drug.builder()
                        .itemSeq(e.getItemSeq())
                        .itemName(e.getItemName())
                        .entpName(e.getEntpName())
                        .eeText(e.getEeDocData())
                        .build()
        ).toList();
        drugRepo.saveAll(domains);
    }

    public List<Drug> search(String q, int page, int size) {
        float[] vec = embedding.getEmbedding(q);
        return drugRepo.searchBySymptoms(q, vec, size, page * size);
    }
}