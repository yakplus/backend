package com.likelion.backendplus4.yakplus.temp.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
public class SymptomController {

	private final ElasticsearchClient esClient;

	@GetMapping("/autocomplete")
	public ResponseEntity<List<String>> autocomplete(@RequestParam String q) throws IOException {
		SearchResponse<Void> resp = esClient.search(s -> s
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

		// Suggest 파싱
		List<String> results = resp.suggest().get("symp_sugg")
			.get(0).completion().options().stream()
			.map(CompletionSuggestOption::text)
			.distinct()
			.toList();

		return ResponseEntity.ok(results);
	}
}