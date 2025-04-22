package com.likelion.backendplus4.yakplus.search.application.service;

import com.likelion.backendplus4.yakplus.search.application.port.in.IndexRequest;
import com.likelion.backendplus4.yakplus.search.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.search.application.port.out.RawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.search.application.port.out.DrugIndexRepositoryPort;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.infrastructure.entity.GovDrugRawDataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugIndexer implements IndexUseCase {
    private final RawDataRepositoryPort rawDataRepositoryPort;
    private final DrugIndexRepositoryPort drugIndexRepositoryPort;

    @Override
    public void index(IndexRequest request) {
        List<Drug> drugs = fetchAndTransformRawData(request);
        saveDrugs(drugs);
    }

    private List<Drug> fetchAndTransformRawData(IndexRequest request) {
        Pageable pageable = createPageable(request.limit());
        List<GovDrugRawDataEntity> rawData = rawDataRepositoryPort.fetchRawData(request.lastSeq(), pageable);
        return convertToDrugDomains(rawData);
    }

    private Pageable createPageable(int limit) {
        return PageRequest.of(0, limit, Sort.by("itemSeq").ascending());
    }

    private List<Drug> convertToDrugDomains(List<GovDrugRawDataEntity> rawData) {
        return rawData.stream()
                .map(this::mapToDrugDomain)
                .collect(Collectors.toList());
    }

    private Drug mapToDrugDomain(GovDrugRawDataEntity entity) {
        return Drug.builder()
                .itemSeq(entity.getItemSeq())
                .itemName(entity.getItemName())
                .entpName(entity.getEntpName())
                .eeText(entity.getEeDocData())
                .build();
    }

    private void saveDrugs(List<Drug> drugs) {
        drugIndexRepositoryPort.saveAll(drugs);
    }
}