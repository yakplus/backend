package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugDetailEntity;

@Repository
public interface ApiDataDrugJPARepo extends JpaRepository<GovDrugDetailEntity, Long> {
}
