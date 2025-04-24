package com.likelion.backendplus4.yakplus.search.application.port.in;

import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;

import java.util.List;

public interface SearchDrugUseCase {
    List<SearchResponse> search(SearchRequest searchRequest);
}