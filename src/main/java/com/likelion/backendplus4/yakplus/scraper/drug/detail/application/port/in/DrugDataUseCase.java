package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in;

import java.util.List;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model.GovDrug;

public interface DrugDataUseCase {
	List<GovDrug> findAllRawDrug();
}
