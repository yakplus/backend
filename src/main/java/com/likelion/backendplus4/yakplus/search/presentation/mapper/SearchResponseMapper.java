package com.likelion.backendplus4.yakplus.search.presentation.mapper;

import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResponseMapper {

    public static SearchResponse toResponse(DrugSearchDomainNatural dsn) {
        return SearchResponse.builder()
                .drugId(dsn.getDrugId())
                .drugName(dsn.getDrugName())
                .company(dsn.getCompany())
                .efficacy(dsn.getEfficacy())
                .imageUrl(dsn.getImageUrl())
                .build();
    }

    public static List<SearchResponse> toResponseList(List<DrugSearchDomainNatural> drugs) {
        return drugs.stream()
                .map(SearchResponseMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static SearchResponseList toResponseListWithCount(List<DrugSearchDomainNatural> drugs) {
        List<SearchResponse> responses = toResponseList(drugs);
        return new SearchResponseList(responses, responses.size());
    }
}
