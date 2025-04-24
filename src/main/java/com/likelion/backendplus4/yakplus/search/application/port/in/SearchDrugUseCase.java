package com.likelion.backendplus4.yakplus.search.application.port.in;

import java.util.List;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;

public interface SearchDrugUseCase {
    List<Drug> search(SearchRequest searchRequest);
}