package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.entity.GovDrugEntity;

/**
 * GovDrugEntity와 도메인 모델(GovDrug) 간의 매핑을 담당하는 클래스.
 * Entity 객체를 비즈니스 도메인 객체로 변환한다.
 *
 * @since 2025-04-30
 */
public class DrugDataMapper {

	/**
	 * GovDrugEntity로부터 GovDrug 도메인 객체를 생성합니다.
	 *
	 * @param e 변환할 GovDrugEntity 객체
	 * @return 변환된 GovDrug 도메인 객체
	 *
	 * @author 함예정
	 * @since 2025-04-30
	 */
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
