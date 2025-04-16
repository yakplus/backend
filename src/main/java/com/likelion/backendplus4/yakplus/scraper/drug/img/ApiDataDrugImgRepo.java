package com.likelion.backendplus4.yakplus.scraper.drug.img;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.ApiDataDrugDetail;

@Repository
public interface ApiDataDrugImgRepo extends JpaRepository<ApiDataDrugImg, Long> {
}
