package com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;

@Repository
public interface ApiDataDrugImgRepo extends JpaRepository<ApiDataDrugImgEntity, Long> {
}
