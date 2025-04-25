package com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 형태의 텍스트 데이터를 파싱하여
 * "text" 또는 "title" 키의 모든 문자열 값을 추출하는 유틸리티 클래스입니다.
 *
 * @author 박찬병
 * @since 2025-04-24
 * @modified 2025-04-25
 */
public class JsonTextParser {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * JSON 문자열에서 "text" 또는 "title" 필드의 모든 텍스트를 재귀적으로 추출하여 리스트로 반환합니다.
	 *
	 * @param json JSON 형식의 문자열
	 * @return 추출된 텍스트 값의 리스트
	 * @throws IOException JSON 파싱 실패 시 발생
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	public static List<String> extractAllTexts(String json) throws IOException {
		// JSON 문자열을 파싱하여 루트 JsonNode 객체를 생성
		JsonNode root = objectMapper.readTree(json);

		// 텍스트 내용을 저장할 리스트 초기화
		List<String> texts = new ArrayList<>();

		// 루트 노드에서 모든 텍스트를 재귀적으로 수집
		collect(root, texts);
		return texts;
	}

	/**
	 * JsonNode를 순회하며 "text"와 "title" 키를 찾아 texts 리스트에 추가합니다.
	 *
	 * @param node JSON 노드
	 * @param texts 텍스트가 수집될 리스트
	 * @author 박찬병
	 * @since 2025-04-24
	 * @modified 2025-04-25
	 */
	private static void collect(JsonNode node, List<String> texts) {
		// JsonNode의 타입(Object/Array)에 따라 분기 처리
		if (node.isObject()) {
			// 객체 노드인 경우, 각 필드(key, value)를 순회
			node.fields().forEachRemaining(e -> {
				String key = e.getKey();
				JsonNode val = e.getValue();
				// "text" 또는 "title" 키를 가진 텍스트 필드를 찾았는지 검사
				if ((key.equals("text") || key.equals("title")) && val.isTextual()) {
					String t = val.asText().trim();
					if (!t.isEmpty() && !t.equals("&nbsp;")) {
						texts.add(t);
					}
				} else {
					// 위 조건이 아니면 해당 값을 다시 검사하도록 재귀 호출
					collect(val, texts);
				}
			});
		} else if (node.isArray()) {
			// 배열 노드인 경우, 각 요소를 재귀적으로 순회
			node.forEach(child -> collect(child, texts));
		}
	}
}
