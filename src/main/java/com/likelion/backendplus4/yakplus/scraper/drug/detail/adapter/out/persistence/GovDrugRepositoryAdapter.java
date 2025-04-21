package com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out.DrugRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GovDrugRepositoryAdapter implements DrugRepositoryPort {
	private final GovDrugJpaRepository govDrungJpaRepository;

	@Override
	public List<GovDrugEntity> findAll() {
		return govDrungJpaRepository.findAll();
	}

}
