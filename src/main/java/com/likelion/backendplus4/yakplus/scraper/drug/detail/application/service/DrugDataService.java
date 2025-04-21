package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.service;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugEntity;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in.DrugDataUseCase;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out.DrugRepositoryPort;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model.GovDrug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class DrugDataService implements DrugDataUseCase {
	private final DrugRepositoryPort repositoryPort;

	@Override
	public List<GovDrug> findAllRawDrug() {
		log.info("findAllRawDrug called");
		return repositoryPort.findAll().stream()
			.map(DrugDataMapper::toDomainFromEntity)
			.collect(toList());
	}
}
