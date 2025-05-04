package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.entity.DrugEntity;
/**
 * 의약품 정보(GovDrugEntity)에 대한 JPA 레포지토리 인터페이스.
 *
 * @since 2025-04-30
 */
public interface DrugJpaRepository extends JpaRepository<DrugEntity, Long> {
}
