package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.search.application.port.out.RawDataRepositoryPort;
import com.likelion.backendplus4.yakplus.search.infrastructure.entity.GovDrugRawDataEntity;
import com.likelion.backendplus4.yakplus.search.infrastructure.repository.GovDrugRawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaGovDrugRawDataAdapter implements RawDataRepositoryPort {
    private final GovDrugRawDataRepository jpaRepo;

    @Override
    public List<GovDrugRawDataEntity> fetchRawData(Long lastSeq, Pageable pageable) {
        long startSeq = (lastSeq == null ? 0L : lastSeq);
        return jpaRepo.findByItemSeqGreaterThanOrderByItemSeqAsc(startSeq, pageable);
    }
}