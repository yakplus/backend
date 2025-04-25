package com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 증상 텍스트 전처리를 위한 유틸리티 클래스입니다.
 * - 번호, 헤더, 기호를 제거하여 단일 문자열로 결합하는 기능
 * - 키워드 자동완성을 위한 토큰 생성 기능
 *
 * @author 박찬병
 * @since 2025-04-24
 * @modified 2025-04-25
 */

public class SymptomTextParser {

	/**
	 * 주어진 문자열 목록에서 번호(“1.”), 헤더(“효능효과”), 기호(“○•▶”)를 제거하고
	 * 하나의 문자열로 결합합니다.
	 *
	 * @param raws 원본 문자열 리스트 (각 줄 단위)
	 * @return 전처리 후 결합된 단일 문자열
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	public static String flattenLines(List<String> raws) {
		// 각 줄에서 번호·헤더·기호를 제거
		return raws.stream()
			.map(line -> line.replaceAll("^\\d+\\.\\s*|효능효과|[○•▶]", " "))
			.collect(Collectors.joining(" "));
	}

	/**
	 * 전처리된 텍스트를 토큰으로 분리하고,
	 * 불용어 및 조사를 제거하여 자동완성용 키워드 리스트를 생성합니다.
	 *
	 * @param text 전처리된 문자열
	 * @return 자동완성용 키워드 리스트
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	public static List<String> tokenizeForSuggestion(String text) {
		// 구분자(쉼표, 구두점, 공백 등)로 텍스트 분할
		return Arrays.stream(text.split("[,·/;:\\s()\\[\\]]+"))
			.map(String::trim)
			// 최소 2자 이상인 토큰만 유지
			.filter(tok -> tok.length() >= 2)
			// 불용어 필터링
			.filter(tok -> !Set.of("특히", "등의", "또는", "및", "의한").contains(tok))
			// 조사(의, 에, 으로 등) 제거
			.map(tok -> tok.replaceAll("(?<base>.+?)(?:의|에|으로|에서|시의)$", "${base}"))
			// 중복 키워드 제거
			.distinct()
			.toList();
	}
}