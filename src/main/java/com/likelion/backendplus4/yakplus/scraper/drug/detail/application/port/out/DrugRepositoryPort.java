package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugEntity;

public interface DrugRepositoryPort {
	List<GovDrugEntity> findAll();
}
