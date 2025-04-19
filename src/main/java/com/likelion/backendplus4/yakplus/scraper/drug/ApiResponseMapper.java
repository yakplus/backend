package com.likelion.backendplus4.yakplus.scraper.drug;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiResponseMapper {

	public static JsonNode getItemsFromResponse(String response) {
		log.info("응답에서 items 값 추출");
		try {
			return new ObjectMapper().readTree(response)
				.path("body")
				.path("items");
		} catch (JsonProcessingException e) {
			log.error("items 추출 실패");
			//TODO: CustomException 만들고, ControllerAdvice로 예외처리 필요
			throw new RuntimeException(e);
		}
	}

	public static int getTotalCountFromResponse(String response) {
		log.info("응답에서 데이터 사이즈 추출");
		try {
			return new ObjectMapper().readTree(response)
				.path("body")
				.path("totalCount")
				.asInt();
		} catch (JsonProcessingException e) {
			log.error("totalCount 추출 실패");
			//TODO: CustomException 만들고, ControllerAdvice로 예외처리 필요
			throw new RuntimeException(e);
		}
	}

}
