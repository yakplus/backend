package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.entity.GovDrugEntity;

public interface GovDrugJpaRepository extends JpaRepository<GovDrugEntity, Long> {
}
