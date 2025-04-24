package com.likelion.backendplus4.yakplus.drug.infrastructure.support.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MaterialParser {
	public static String parseMaterial(String raw) throws Exception {
		ObjectMapper result = new ObjectMapper();
		ArrayNode resultArray = result.createArrayNode();
		String[] blocks = splitBlock(raw);
		parsingblocksAndPutArrayItem(blocks, resultArray);
		return convertString(result, resultArray);
	}

	private static void parsingblocksAndPutArrayItem(String[] blocks, ArrayNode resultArray) {
		for (String block : blocks) {
			block = block.trim();
			if (block.isEmpty()) {
				continue;
			}
			String[] pairs = splitByPipe(block);
			ObjectNode item = makeItem(pairs);
			resultArray.add(item);
		}
	}

	private static String convertString(ObjectMapper objectMapper, ArrayNode arrayNode) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
		} catch (JsonProcessingException e) {
			//TODO String 변환실패
			throw new RuntimeException(e);
		}
	}

	private static ObjectNode makeItem(String[] pairs) {
		ObjectNode item = new ObjectMapper().createObjectNode();
		for (String pair : pairs) {
			String[] kv = pair.split(":", 2);
			String key = kv[0].trim();
			String value = "";
			if(kv.length == 2){
				value = kv[1].trim();
			}
			item.put(key, value);
		}
		return item;
	}

	private static String[] splitByPipe(String block) {
		return block.split("\\|");
	}

	private static String[] splitBlock(String raw) {
		return raw.split(";");
	}
}
