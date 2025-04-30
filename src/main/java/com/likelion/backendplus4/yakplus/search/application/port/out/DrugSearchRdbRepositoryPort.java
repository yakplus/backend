package com.likelion.backendplus4.yakplus.search.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;

public interface DrugSearchRdbRepositoryPort {
	Page<GovDrug> findAllDrugs(Pageable pageable);

	Drug findById(Long id);
}
