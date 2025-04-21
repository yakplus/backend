package com.likelion.backendplus4.yakplus.domain.model;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;

@Builder
public class GovDrugDetail {
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

	public JsonNode toJson(String json) {
		try {
			return new ObjectMapper().readValue(json, JsonNode.class);
		} catch (JsonProcessingException e) {
			//TODO 에러 로그 처리 필요합니다.
			throw new RuntimeException(e);
		}
	}
}
