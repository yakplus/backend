package com.likelion.backendplus4.yakplus.search.application.port.in;

import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.AutoCompleteStringList;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;

import java.util.List;

public interface SearchDrugUseCase {
    List<SearchResponse> search(SearchRequest searchRequest);

    AutoCompleteStringList getSymptomAutoComplete(String q);

    AutoCompleteStringList getDrugNameAutoComplete(String q);

    SearchResponseList searchDrugByDrugName(String q, int page, int size);

    SearchResponseList searchDrugBySymptom(String q, int page, int size);

}