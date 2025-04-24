package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;

import jakarta.transaction.Transactional;

@Repository
public interface GovDrugDetailJpaRepository extends JpaRepository<GovDrugDetailEntity,Long> {

	@Override
	@Transactional
	<S extends GovDrugDetailEntity> List<S> saveAllAndFlush(Iterable<S> entities);
}