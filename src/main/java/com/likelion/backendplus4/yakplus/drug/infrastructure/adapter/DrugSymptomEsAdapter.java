package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;


import static org.elasticsearch.client.security.user.privileges.Role.IndexPrivilegeName.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugSymptom;
import com.likelion.backendplus4.yakplus.drug.exception.EsSuggestException;
import com.likelion.backendplus4.yakplus.drug.exception.error.EsErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.elasticsearch.DrugSymptomRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.SymptomMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Elasticsearch를 통해 약품 증상 문서를 색인하고,
 * 자동완성 제안 결과를 제공하는 어댑터입니다.
 *
 * @author 박찬병
 * @since 2025-04-24
 * @modified 2025-04-25
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DrugSymptomEsAdapter {

	private final DrugSymptomRepository symptomRepository;
	private final ElasticsearchClient esClient;

	/**
	 * 주어진 증상 문서 리스트를 Elasticsearch에 색인합니다.
	 *
	 * @param docs 색인할 DrugSymptomDocument 객체 리스트
	 * @author 박찬병
	 * @modified 2025-04-25
	 * @since 2025-04-24
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveAll(List<DrugSymptomDocument> docs) {
		symptomRepository.saveAll(docs);
	}

	/**
	 * 사용자 입력 키워드를 바탕으로 Elasticsearch Suggest API를 호출해
	 * 자동완성 추천 단어 리스트를 반환합니다.
	 *
	 * @param q 사용자 입력 문자열
	 * @return 추천 키워드 리스트
	 * @throws EsSuggestException 자동완성 API 호출 실패 시 발생
	 * @author 박찬병
	 * @modified 2025-04-25
	 * @since 2025-04-24
	 */
	public List<String> getSearchAutoCompleteResponse(String q) {
		SearchResponse<Void> resp;
		try {
			resp = esClient.search(s -> s
					.index("eedoc")
					.suggest(su -> su
						.suggesters("symp_sugg", sg -> sg
							.prefix(q)
							.completion(c -> c
								.field("symptomSuggester")
								.analyzer("symptom_search_autocomplete")  // ← 이 줄만 추가
								.size(20)
							)
						)
					)
				, Void.class);
		} catch (IOException e) {
			throw new EsSuggestException(EsErrorCode.ES_SUGGEST_SEARCH_FAIL);
		}

		// Suggest 파싱
		return resp.suggest().get("symp_sugg")
			.get(0).completion().options().stream()
			.map(CompletionSuggestOption::text)
			.distinct()
			.toList();
	}

	/**
	 * 검색어에 매칭되는 증상 문서 리스트를 Elasticsearch에서 조회합니다.
	 *
	 * @param q    검색어 프리픽스
	 * @param page 조회할 페이지 번호 (0부터 시작)
	 * @param size 페이지 당 문서 수
	 * @return 증상 문서 리스트
	 * @throws EsSuggestException 검색 중 오류 발생 시
	 */
	public List<DrugSymptom> searchDocsBySymptom(String q, int page, int size) {
		try {
			SearchResponse<DrugSymptomDocument> resp = esClient.search(s -> s
					.index("eedoc")
					.from(page * size)
					.size(size)
					.query(qb -> qb
						.match(m -> m
							.field("symptom")   // only_nouns analyzer 적용된 필드
							.query(q)           // 사용자가 입력한 q 값
						)
					)
				, DrugSymptomDocument.class);
			return resp.hits().hits().stream()
				.map(Hit::source)
				.filter(Objects::nonNull)
				.map(SymptomMapper::toDomain)
				.toList();
		} catch (IOException e) {
			log.error("ES 증상 문서 검색 실패: q={}", q, e);
			throw new EsSuggestException(EsErrorCode.ES_SEARCH_FAIL);
		}
	}
}
