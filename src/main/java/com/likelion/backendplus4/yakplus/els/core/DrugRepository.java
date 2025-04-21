package com.likelion.backendplus4.yakplus.els.core;


import java.util.List;

public interface DrugRepository {
    void saveAll(List<Drug> drugs);

    List<Drug> searchBySymptoms(String query, float[] vector, int size, int from);
}
