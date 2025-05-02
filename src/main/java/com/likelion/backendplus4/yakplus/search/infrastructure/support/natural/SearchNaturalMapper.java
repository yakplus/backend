package com.likelion.backendplus4.yakplus.search.infrastructure.support.natural;

import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;

public class SearchNaturalMapper {

    public static DrugSearchNatural toDrugSearchNatural(String query, int page, int size) {
        return DrugSearchNatural.builder()
                .query(query)
                .page(page)
                .size(size)
                .build();
    }
}
