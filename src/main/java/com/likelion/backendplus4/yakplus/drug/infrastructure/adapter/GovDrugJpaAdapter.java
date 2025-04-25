package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;

import lombok.RequiredArgsConstructor;

/**
 * DB에서 약품 데이터를 다루는 어댑터입니다.
 *
 * @author 박찬병
 * @since 2025-04-24
 * @modified 2025-04-25
 */
@Component
@RequiredArgsConstructor
public class GovDrugJpaAdapter {

	private final GovDrugJpaRepository drugJpaRepository;

	/**
	 * 주어진 Pageable 정보에 따라 DB에서 한 페이지 분량의 GovDrugEntity를 조회하고,
	 * 각 엔티티를 도메인 모델(GovDrug)로 변환하여 Page 형태로 반환합니다.
	 *
	 * @param pageable 조회할 페이지 번호와 크기를 포함하는 Pageable 객체
	 * @return 페이지 단위로 변환된 GovDrug 도메인 객체의 Page
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 *
	 */
	public Page<GovDrug> findAllDrugs(Pageable pageable) {
		return drugJpaRepository.findAll(pageable)
			.map(DrugDataMapper::toDomainFromEntity);
	}
}
