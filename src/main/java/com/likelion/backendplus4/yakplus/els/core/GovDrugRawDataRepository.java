package com.likelion.backendplus4.yakplus.els.core;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovDrugRawDataRepository extends JpaRepository<GovDrugRawData, Long> {
    List<GovDrugRawData> findTop100ByOrderByItemSeqAsc();
}
