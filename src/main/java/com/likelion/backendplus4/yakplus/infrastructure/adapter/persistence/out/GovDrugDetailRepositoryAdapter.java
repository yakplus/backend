package com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.out;

import java.util.List;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.application.port.out.DrugDetailRepositoryPort;
import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;
import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.GovDrungDetailJpaRepository;
import com.likelion.backendplus4.yakplus.domain.model.GovDrugDetail;

import jakarta.transaction.Transactional;
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
	@Transactional
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
