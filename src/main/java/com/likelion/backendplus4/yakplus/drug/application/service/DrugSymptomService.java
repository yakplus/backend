package com.likelion.backendplus4.yakplus.drug.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.DrugSymptomEsAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.GovDrugJpaAdapter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.EntityDocMapper;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.dto.DrugSymptomSearchListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 약품 증상 데이터를 처리하는 서비스입니다.
 * @sice 2025-04-24
 * @modified 2025-04-25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrugSymptomService {

	private static final int CHUNK_SIZE = 1_000;

	private final GovDrugJpaAdapter drugJpaAdapter;
	private final DrugSymptomEsAdapter symptomAdapter;

    /**
     * DB에서 약품 데이터를 페이징으로 가져와 Elasticsearch에 일괄 색인합니다.
     * 각 페이지는 CHUNK_SIZE만큼 처리되며, 모든 데이터를 순차적으로 색인합니다.
     *
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-25
     */
	public void indexAll() {
		int page = 0;
		Page<GovDrug> drugPage;

		do {
			// 1. 페이징으로 DB에서 한 청크 가져오기
			drugPage = drugJpaAdapter.findAllDrugs(PageRequest.of(page, CHUNK_SIZE));

			// 2. 도메인 → ES Document 변환
			List<DrugSymptomDocument> docs = drugPage.stream()
				.map(EntityDocMapper::toDocument)  // 내부에서 예외 처리 됨
				.toList();

			// 3. 청크별 ES에 색인
			symptomAdapter.saveAll(docs);

			// 4. 다음 1000개 값 루프
			page++;
		} while (drugPage.hasNext());
	}

    /**
     * 주어진 사용자 입력 문자열을 바탕으로 증상 자동완성 키워드를 가져옵니다.
     * Elasticsearch에서 Suggest API 등을 활용하여 추천 결과를 반환합니다.
     *
     * @param q 사용자 입력 문자열
     * @return 자동완성 추천 결과 리스트 DTO
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-25
     */
    public DrugSymptomSearchListResponse getSymptomAutoComplete(String q) {
        return new DrugSymptomSearchListResponse(symptomAdapter.getSearchAutoCompleteResponse(q));
    }
}
