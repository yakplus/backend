package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import java.io.IOException;
import java.util.List;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.GovDrugEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.SymptomTextParser;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser.JsonTextParser;

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

	/**
	 * DB 엔티티 → ES Document 변환
	 */
	public static DrugSymptomDocument toDocument(GovDrug entity) throws IOException {
		List<String> raws = JsonTextParser.extractAllTexts(entity.getEfficacy());

		String flatText = SymptomTextParser.flattenLines(raws);
		List<String> suggestTokens = SymptomTextParser.tokenizeForSuggestion(flatText);

		return DrugSymptomDocument.builder()
			.drugId(entity.getDrugId())
			.drugName(entity.getDrugName())
			.symptom(flatText)
			.symptomSuggester(suggestTokens)
			.build();
	}
}
