package com.likelion.backendplus4.yakplus.search.application.port.out;

import java.util.List;

import org.springframework.data.domain.Page;

import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;

public interface DrugSearchRepositoryPort {
    List<Drug> searchBySymptoms(String query, float[] vector, int from, int size);

    List<String> getSymptomAutoCompleteResponse(String q);

    Page<DrugSearchDomain> searchDocsBySymptom(String q, int page, int size);

    // 1) 아이템명 자동완성
    List<String> getDrugNameAutoCompleteResponse(String q);

    // 2) 아이템명 매칭 문서 검색
    Page<DrugSearchDomain> searchDocsByItemName(String q, int page, int size);
}