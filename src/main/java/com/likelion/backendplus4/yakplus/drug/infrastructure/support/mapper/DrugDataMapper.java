package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.MaterialInfo;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.entity.GovDrugEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;

public class DrugDataMapper {
	public static GovDrug toDomainFromEntity(GovDrugEntity e) {
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
