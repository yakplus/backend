package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

import com.likelion.backendplus4.yakplus.drug.exception.EsSuggestException;
import com.likelion.backendplus4.yakplus.drug.exception.error.EsErrorCode;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.document.DrugSymptomDocument;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.elasticsearch.DrugSymptomRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DrugSymptomEsAdapter {

	private final DrugSymptomRepository symptomRepository;
	private final ElasticsearchClient esClient;


	@Transactional
	public void saveAll(List<DrugSymptomDocument> docs) {
		symptomRepository.saveAll(docs);
	}

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
}

