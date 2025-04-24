// package com.likelion.backendplus4.yakplus.temp.application;
//
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Objects;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.GovDrungDetailJpaRepository;
// import com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.entity.GovDrugDetailEntity;
// import com.likelion.backendplus4.yakplus.temp.dao.EEDocRepository;
// import com.likelion.backendplus4.yakplus.temp.util.JsonTextExtractor;
// import com.likelion.backendplus4.yakplus.temp.entity.document.EEDocDocument;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class EEDocIndexService {
//
// 	private final GovDrungDetailJpaRepository drugRepository;
// 	private final EEDocRepository docRepository;
//
// 	@Transactional
// 	public void indexAll() {
// 		// TODO 배치 처리
// 		List<EEDocDocument> docs = drugRepository.findAll().stream()
// 			.map(this::toDocument)
// 			.filter(Objects::nonNull)
// 			.toList();
//
// 		docRepository.saveAll(docs);
// 	}
//
// 	/**
// 	 * DB 엔티티 → ES Document 변환
// 	 */
// 	private EEDocDocument toDocument(GovDrugDetailEntity entity) {
// 		List<String> raws = extractRawLines(entity);
// 		if (raws == null)
// 			return null;
//
// 		String flatText = flattenLines(raws);
// 		List<String> suggestTokens = tokenizeForSuggestion(flatText);
//
// 		return EEDocDocument.builder()
// 			.drugId(entity.getDrugId().toString())
// 			.drugName(entity.getDrugName())
// 			.symptom(flatText)
// 			.symptomSuggester(suggestTokens)
// 			.build();
// 	}
//
// 	/**
// 	 * JSON 파싱 및 텍스트 추출
// 	 */
// 	private List<String> extractRawLines(GovDrugDetailEntity entity) {
// 		try {
// 			return JsonTextExtractor.extractAllTexts(entity.getEfficacy());
// 		} catch (IOException ex) {
// 			log.warn("효능효과 JSON 파싱 실패 id={}", entity.getDrugId());
// 			return null;
// 		}
// 	}
//
// 	/**
// 	 * 번호·헤더·기호 제거 후 한 줄로 합치기
// 	 */
// 	private String flattenLines(List<String> raws) {
// 		return raws.stream()
// 			.map(line -> line.replaceAll("^\\d+\\.\\s*|효능효과|[○•▶]", " "))
// 			.collect(Collectors.joining(" "));
// 	}
//
// 	/**
// 	 * 자동완성/제안용 토큰 생성
// 	 */
// 	private List<String> tokenizeForSuggestion(String text) {
// 		return Arrays.stream(text.split("[,·/;:\\s()\\[\\]]+"))
// 			.map(String::trim)
// 			// 길이 2자 이상
// 			.filter(tok -> tok.length() >= 2)
// 			// 불용어 제거
// 			.filter(tok -> !Set.of("특히", "등의", "또는", "및", "의한").contains(tok))
// 			// 조사 제거
// 			.map(tok -> tok.replaceAll("(?<base>.+?)(?:의|에|으로|에서|시의)$", "${base}"))
// 			// 중복 제거
// 			.distinct()
// 			.toList();
// 	}
// }