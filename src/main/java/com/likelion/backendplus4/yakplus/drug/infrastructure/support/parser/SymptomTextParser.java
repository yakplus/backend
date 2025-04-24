package com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SymptomTextParser {

	/** 번호·헤더·기호 제거 후 한 줄로 합치기 */
	public static String flattenLines(List<String> raws) {
		return raws.stream()
			.map(line -> line.replaceAll("^\\d+\\.\\s*|효능효과|[○•▶]", " "))
			.collect(Collectors.joining(" "));
	}

	/** 자동완성/제안용 토큰 생성 */
	public static List<String> tokenizeForSuggestion(String text) {
		return Arrays.stream(text.split("[,·/;:\\s()\\[\\]]+"))
			.map(String::trim)
			// 길이 2자 이상
			.filter(tok -> tok.length() >= 2)
			// 불용어 제거
			.filter(tok -> !Set.of("특히", "등의", "또는", "및", "의한").contains(tok))
			// 조사 제거
			.map(tok -> tok.replaceAll("(?<base>.+?)(?:의|에|으로|에서|시의)$", "${base}"))
			// 중복 제거
			.distinct()
			.toList();
	}
}