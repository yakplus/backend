package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GovDrugJpaAdapter {

	private final GovDrugJpaRepository drugJpaRepository;

	/** 페이징 처리된 도메인 객체 리스트 반환 */
	public Page<GovDrug> findAllDrugs(Pageable pageable) {
		return drugJpaRepository.findAll(pageable)
			.map(DrugDataMapper::toDomainFromEntity);
	}
}
