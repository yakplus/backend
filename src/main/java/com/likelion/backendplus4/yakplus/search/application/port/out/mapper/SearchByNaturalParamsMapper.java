package com.likelion.backendplus4.yakplus.search.application.port.out.mapper;

import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByNaturalParams;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;

public class SearchByNaturalParamsMapper {
    public static SearchByNaturalParams toParams(DrugSearchNatural natural, float[] embeddings) {
        int from = natural.getPage() * natural.getSize();
        return new SearchByNaturalParams(natural.getQuery(), embeddings, from, natural.getSize());
    }
}
