package com.likelion.backendplus4.yakplus.scraper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDataDrungRepo extends JpaRepository<ApiDataDrugDetail, Long> {
}
