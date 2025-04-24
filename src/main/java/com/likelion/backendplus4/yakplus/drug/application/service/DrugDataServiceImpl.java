package com.likelion.backendplus4.yakplus.drug.application.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class DrugDataServiceImpl implements DrugDataService {
	private final GovDrugJpaRepository govDrugJpaRepository;

	@Override
	public List<GovDrug> findAllRawDrug() {
		log.info("findAllRawDrug called");
		return govDrugJpaRepository.findAll().stream()
			.map(DrugDataMapper::toDomainFromEntity)
			.collect(toList());
	}
}
