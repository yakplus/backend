package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.service;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugEntity;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model.GovDrug;

public class DrugDataMapper {
	public static GovDrug toDomainFromEntity(GovDrugEntity e){
		return GovDrug.builder()
			.drugId(e.getId())
			.drugName(e.getDrugName())
			.company(e.getCompany())
			.permitDate(e.getPermitDate())
			.isGeneral(e.isGeneral())
			.materialInfo(e.getMaterialInfo())
			.storeMethod(e.getStoreMethod())
			.validTerm(e.getValidTerm())
			.efficacy(e.getEfficacy())
			.usage(e.getUsage())
			.precaution(e.getPrecaution())
			.imageUrl(e.getImageUrl())
			.build();
	}
}
