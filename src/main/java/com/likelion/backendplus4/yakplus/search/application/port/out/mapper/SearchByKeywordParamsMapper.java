package com.likelion.backendplus4.yakplus.search.application.port.out.mapper;

import com.likelion.backendplus4.yakplus.search.application.port.out.dto.SearchByKeywordParams;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchNatural;

public class SearchByKeywordParamsMapper {

    public static SearchByKeywordParams toParams(DrugSearchNatural drugSearchNatural) {
        return SearchByKeywordParams.builder()
                .query(drugSearchNatural.getQuery())
                .size(drugSearchNatural.getSize())
                .from(drugSearchNatural.getPage())
                .build();
    }
}
