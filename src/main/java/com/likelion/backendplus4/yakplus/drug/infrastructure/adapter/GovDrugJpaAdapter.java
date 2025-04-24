package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GovDrugJpaAdapter {

	private final GovDrugJpaRepository drugJpaRepository;

	public List<GovDrug> findAllDrugs() {
		return drugJpaRepository.findAll().stream()
			.map(DrugDataMapper::toDomainFromEntity)
			.toList();
	}
}
