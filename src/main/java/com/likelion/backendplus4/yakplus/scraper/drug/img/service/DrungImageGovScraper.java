package com.likelion.backendplus4.yakplus.scraper.drug.img.service;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.gov.ApiUriCompBuilder;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.ApiDataDrugJPARepo;
import com.likelion.backendplus4.yakplus.scraper.drug.img.repository.ApiDataDrugImgRepo;
import com.likelion.backendplus4.yakplus.scraper.drug.img.repository.ApiDataDrugImgEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrungImageGovScraper {
	private final ApiUriCompBuilder uriCompBuilder;
	private final RestTemplate restTemplate;
	private final ApiDataDrugImgRepo imgRepo;
	private final ObjectMapper objectMapper;
	//TODO: 추후 삭제
	private final ApiDataDrugJPARepo detailRepo;

	@Transactional
	public void getApiData(){
		log.info("의약품 개요 정보 API 호출 시작");

		URI uriForImgApi = uriCompBuilder.getUriForImgApi();

		String response = restTemplate.getForObject(uriForImgApi, String.class);
		JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
		List<ApiDataDrugImgEntity> imgDatas = null;
		try {
			imgDatas = objectMapper.readValue(items.toString(),
				new TypeReference<List<ApiDataDrugImgEntity>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		imgRepo.saveAllAndFlush(imgDatas);
	}
}
