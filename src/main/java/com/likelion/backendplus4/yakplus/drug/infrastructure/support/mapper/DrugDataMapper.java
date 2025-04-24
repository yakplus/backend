package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;

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
