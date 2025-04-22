package com.likelion.backendplus4.yakplus.scraper.drug.img.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDataDrugImgRepo extends JpaRepository<ApiDataDrugImgEntity, Long> {
}
