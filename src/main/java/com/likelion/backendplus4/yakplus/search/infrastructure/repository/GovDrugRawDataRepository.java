package com.likelion.backendplus4.yakplus.search.infrastructure.repository;

import com.likelion.backendplus4.yakplus.search.infrastructure.entity.GovDrugRawDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovDrugRawDataRepository extends JpaRepository<GovDrugRawDataEntity, Long> {
    List<GovDrugRawDataEntity> findByItemSeqGreaterThanOrderByItemSeqAsc(Long lastSeq, Pageable pageable);
}