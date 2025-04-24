package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.WarningType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GovDrug {
	private Long drugId;
	private String drugName;
	private String company;
	private LocalDate permitDate;
	private boolean isGeneral;
	private String materialInfo;
	private String storeMethod;
	private String validTerm;
	private String efficacy;
	private String usage;
	private String precaution;
	private String imageUrl;

	public List<Material> getMaterialInfo() {
		List<Material> matrerials = new ArrayList<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode json = objectMapper.readTree(materialInfo);

			if (json.isArray()) {
				for (JsonNode node : json) {
					Material ingredient = objectMapper.treeToValue(node, Material.class);
					matrerials.add(ingredient);
				}
			}
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}


		return matrerials;
	}

	// public List<String> getEfficacy() {
	// 	List<String> efficacys = new ArrayList<>();
	// 	try {
	// 		ObjectMapper objectMapper = new ObjectMapper();
	// 		JsonNode json = objectMapper.readTree(this.efficacy);
	// 		for (JsonNode section : json.get("sections")) {
	// 			for (JsonNode article : section.get("articles")) {
	// 				for (JsonNode paragraph : article.get("paragraphs")) {
	// 					efficacys.add(paragraph.get("text").asText());
	// 				}
	// 			}
	// 		}
	// 	} catch (JsonProcessingException e) {
	// 		//TODO: 예외처리
	// 		throw new RuntimeException(e);
	// 	}
	// 	return efficacys;
	// }

	public Map<WarningType, List<String>> getPrecaution() {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<WarningType, List<String>> result = new LinkedHashMap<>();

		try {
			JsonNode json = objectMapper.readTree(this.precaution);
			JsonNode articles = json.get("sections").get(0).get("articles");

			for (JsonNode article : articles) {
				String rawTitle = article.get("title").asText();
				WarningType type = WarningType.fromLabel(rawTitle);

				List<String> texts = new ArrayList<>();
				for (JsonNode paragraph : article.get("paragraphs")) {
					texts.add(paragraph.get("text").asText());
				}

				result.put(type, texts);
			}

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}


		return result;
	}
}
