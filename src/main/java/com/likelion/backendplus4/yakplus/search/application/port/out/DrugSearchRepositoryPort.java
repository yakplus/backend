package com.likelion.backendplus4.yakplus.search.application.port.out;

import java.util.List;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;

public interface DrugSearchRepositoryPort {
    List<Drug> searchBySymptoms(String query, float[] vector, int from, int size);
}