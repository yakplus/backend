package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.DrugSymptomEsAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.GovDrugJpaAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugDataMapper;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomSearchListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugSymptomService {

	private static final int CHUNK_SIZE = 1_000;

	private final GovDrugJpaAdapter drugJpaAdapter;
	private final DrugSymptomEsAdapter symptomAdapter;

	public void indexAll() {
		int page = 0;
		Page<GovDrug> drugPage;

		do {
			// 1. 페이징으로 DB에서 한 청크 가져오기
			drugPage = drugJpaAdapter.findAllDrugs(PageRequest.of(page, CHUNK_SIZE));

			// 2. 도메인 → ES Document 변환
			List<DrugSymptomDocument> docs = drugPage.stream()
				.map(DrugDataMapper::toDocument)  // 내부에서 예외 처리 됨
				.toList();

			// 3. 청크별 ES에 색인
			symptomAdapter.saveAll(docs);

			// 4. 다음 1000개 값 루프
			page++;
		} while (drugPage.hasNext());
	}

	public DrugSymptomSearchListResponse getSymptomAutoComplete(String q) {
		return new DrugSymptomSearchListResponse(symptomAdapter.getSearchAutoCompleteResponse(q));
	}
}
