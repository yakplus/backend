package com.likelion.backendplus4.yakplus.search.application.port.out;

import java.util.List;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSymptom;

public interface DrugSearchRepositoryPort {
    List<Drug> searchBySymptoms(String query, float[] vector, int from, int size);

    List<String> getSymptomAutoCompleteResponse(String q);

    List<DrugSymptom> searchDocsBySymptom(String q, int page, int size);
}