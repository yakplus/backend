package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.search.application.port.in.IndexRequest;
import com.likelion.backendplus4.yakplus.search.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.RawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugIndexer implements IndexUseCase {
    private final RawDataRepositoryPort rawDataRepositoryPort;
    private final DrugIndexRepositoryPort drugIndexRepositoryPort;
    private static final String SORT_BY_PROPERTY = "itemSeq";

    @Override
    public void index(IndexRequest request) {
        Pageable pageable = createPageable(request.limit());
        List<Drug> drugs = fetchAndTransformRawData(request, pageable);
        saveDrugs(drugs);
    }

    private List<Drug> fetchAndTransformRawData(IndexRequest request, Pageable pageable) {
        return rawDataRepositoryPort.fetchRawData(request.lastSeq(), pageable);
    }

    private Pageable createPageable(int limit) {
        return PageRequest.of(0, limit, Sort.by(SORT_BY_PROPERTY).ascending());
    }

    private void saveDrugs(List<Drug> drugs) {
        drugIndexRepositoryPort.saveAll(drugs);
    }
}