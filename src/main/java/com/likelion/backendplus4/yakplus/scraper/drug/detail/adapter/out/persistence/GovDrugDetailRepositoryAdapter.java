package com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model.GovDrugDetail;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GovDrugDetailRepositoryAdapter implements DrugDetailRepositoryPort {
	private final GovDrungDetailJpaRepository govDrungDetailJpaRepository;

	@Override
	public List<GovDrugDetailEntity> findAll(String code) {
		return govDrungDetailJpaRepository.findAll();
	}

	@Override
	public void saveAllAndFlush(List<GovDrugDetailEntity> entities) {
		govDrungDetailJpaRepository.saveAllAndFlush(entities);
	}

	public GovDrugDetail toDomainFromEntity(GovDrugDetailEntity detail){
		return GovDrugDetail.builder()
			.drugId(detail.getDrugId())
			.drugName(detail.getDrugName())
			.company(detail.getCompany())
			.permitDate(detail.getPermitDate())
			.isGeneral(detail.isGeneral())
			.materialInfo(detail.getMaterialInfo())
			.storeMethod(detail.getStoreMethod())
			.validTerm(detail.getValidTerm())
			.efficacy(detail.getEfficacy())
			.usage(detail.getUsage())
			.precaution(detail.getPrecaution())
			.build();
	}
}
