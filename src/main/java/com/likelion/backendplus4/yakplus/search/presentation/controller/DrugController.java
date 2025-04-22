package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.search.application.port.in.IndexRequest;
import com.likelion.backendplus4.yakplus.search.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.in.SearchDrugUseCase;
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

    @GetMapping("/search-all")
    public List<Drug> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchDrugUseCase.search(q, page, size);
    }
}