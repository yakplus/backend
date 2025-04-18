package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugDetailEntity;

public interface DrugDetailRepositoryPort {

	List<GovDrugDetailEntity> findAll(String code);
	void saveAllAndFlush(List<GovDrugDetailEntity> entities);
}
