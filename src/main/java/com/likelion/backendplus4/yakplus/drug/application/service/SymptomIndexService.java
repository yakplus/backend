package com.likelion.backendplus4.yakplus.drug.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.DrugSymptomEsAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.GovDrugJpaAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomIndexService {
	private final GovDrugJpaAdapter drugJpaAdapter;
	private final DrugSymptomEsAdapter symptomAdapter;

	@Transactional
	public void indexAll() {
		List<GovDrug> drugs = drugJpaAdapter.findAllDrugs();

		List<DrugSymptomDocument> docs = drugs.stream()
			.map(domain -> {
				try {
					return DrugDataMapper.toDocument(domain);
				} catch (IOException ex) {
					log.warn("파싱 실패 id={}", domain.getDrugId(), ex);
					throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
				}
			})
			.filter(Objects::nonNull)
			.toList();

		symptomAdapter.saveAll(docs);
	}

	public List<String> getSymptomAutoComplete(String q) {
		return symptomAdapter.getSearchAutoCompleteResponse(q);
	}
}
