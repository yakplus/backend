package com.likelion.backendplus4.yakplus.application.port.out;

import java.util.List;

import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;

public interface DrugDetailRepositoryPort {

	List<GovDrugDetailEntity> findAll(String code);
	void saveAllAndFlush(List<GovDrugDetailEntity> entities);
}
