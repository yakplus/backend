package com.likelion.backendplus4.yakplus.search.application.port.out;

import java.util.List;

import org.springframework.data.domain.Page;

import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;

public interface DrugSearchRepositoryPort {
    List<Drug> searchBySymptoms(String query, float[] vector, int from, int size);

    List<String> getSymptomAutoCompleteResponse(String q);

    Page<DrugSearchDomain> searchDocsBySymptom(String q, int page, int size);

    List<String> getDrugNameAutoCompleteResponse(String q);

    Page<DrugSearchDomain> searchDocsByDrugName(String q, int page, int size);

    List<String> getIngredientAutoCompleteResponse(String q);

    Page<DrugSearchDomain> searchDocsByIngredient(String q, int page, int size);
}