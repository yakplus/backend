package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.search.application.port.out.RawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.infrastructure.entity.GovDrugRawDataEntity;
import com.likelion.backendplus4.yakplus.search.infrastructure.repository.GovDrugRawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaGovDrugRawDataAdapter implements RawDataRepositoryPort {
    private final GovDrugRawDataRepository jpaRepo;

    @Override
    public List<Drug> fetchRawData(Long lastSeq, Pageable pageable) {
        long startSeq = getStartSeq(lastSeq);
        List<GovDrugRawDataEntity> govDrugRawDataEntities = jpaRepo.findByItemSeqGreaterThanOrderByItemSeqAsc(startSeq, pageable);

        return convertToDrugDomains(govDrugRawDataEntities);
    }

    private Long getStartSeq(Long lastSeq) {
        return (lastSeq == null ? 0L : lastSeq);
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
}