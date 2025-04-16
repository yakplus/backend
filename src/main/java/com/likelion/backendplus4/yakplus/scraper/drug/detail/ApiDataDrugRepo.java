package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDataDrugRepo extends JpaRepository<ApiDataDrugDetail, Long> {
}
