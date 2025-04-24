package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.search.application.port.in.IndexRequest;
import com.likelion.backendplus4.yakplus.search.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchRequest;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {
    private final IndexUseCase indexUseCase;
    private final SearchDrugUseCase searchDrugUseCase;

    @PostMapping("/index")
    public void reindex(@RequestBody IndexRequest request) {
        indexUseCase.index(request);
    }

    @GetMapping("/search")
    public List<Drug> search(@RequestBody SearchRequest searchRequest) {
        return searchDrugUseCase.search(searchRequest);
    }
}