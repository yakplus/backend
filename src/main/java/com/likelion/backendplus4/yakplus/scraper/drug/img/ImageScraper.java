package com.likelion.backendplus4.yakplus.scraper.drug.img;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.gov.ApiUriCompBuilder;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugDetailEntity;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.ApiDataDrugJPARepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageScraper {
	private final ApiUriCompBuilder uriCompBuilder;
	private final RestTemplate restTemplate;
	private final ApiDataDrugImgRepo imgRepo;
	private final ObjectMapper objectMapper;
	//TODO: 추후 삭제
	private final ApiDataDrugJPARepo detailRepo;

	@Transactional
	public void getApiData(){
		log.info("의약품 개요 정보 API 호출 시작");

		// URI uriForImgApi = uriCompBuilder.getUriForImgApi();
		// ApiDataDrugImg object = restTemplate.getForObject(uriForImgApi,
		// 														new TypeReference<List<uriForImgApi>>() {});


		//100번 반복
		List<GovDrugDetailEntity> oldDatas = detailRepo.findAll();
		List<ApiDataDrugImg> imgDatas = new ArrayList<>();

		for (GovDrugDetailEntity oldData : oldDatas) {
			System.out.println("oldData = " + oldData.getDrugId());
			URI uri = uriCompBuilder.getUriForImgApiBySeq(oldData.getDrugId().toString());
			String response = restTemplate.getForObject(uri, String.class);
			JsonNode items = ApiResponseMapper.getItemsFromResponse(response);

			try {
				if(items.isArray()){
					for (JsonNode item : items) {
						ApiDataDrugImg data = objectMapper.readValue(item.toString(),
							ApiDataDrugImg.class);
						System.out.println("data = " + data);
						imgDatas.add(data);
					}
				}
			} catch (JsonProcessingException e) {
				log.error("객체 맵핑 실패");
				throw new RuntimeException(e);
			}
		}
		System.out.println("imgDatas.size() = " + imgDatas.size());
		imgRepo.saveAllAndFlush(imgDatas);
	}
}
