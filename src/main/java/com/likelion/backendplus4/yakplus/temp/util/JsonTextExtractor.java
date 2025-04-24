package com.likelion.backendplus4.yakplus.temp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTextExtractor {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static List<String> extractAllTexts(String json) throws IOException {
		JsonNode root = objectMapper.readTree(json);
		List<String> texts = new ArrayList<>();
		collect(root, texts);
		return texts;
	}

	private static void collect(JsonNode node, List<String> texts) {
		if (node.isObject()) {
			node.fields().forEachRemaining(e -> {
				String key = e.getKey();
				JsonNode val = e.getValue();
				if ((key.equals("text") || key.equals("title")) && val.isTextual()) {
					String t = val.asText().trim();
					if (!t.isEmpty() && !t.equals("&nbsp;")) {
						texts.add(t);
					}
				} else {
					collect(val, texts);
				}
			});
		} else if (node.isArray()) {
			node.forEach(child -> collect(child, texts));
		}
	}
}
