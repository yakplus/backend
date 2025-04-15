package com.likelion.backendplus4.yakplus.domain.drug.application;

import com.likelion.backendplus4.yakplus.domain.drug.dao.DrugProductRawRepository;
import com.likelion.backendplus4.yakplus.domain.drug.dto.DrugProductRawDto;
import com.likelion.backendplus4.yakplus.domain.drug.entity.DrugProductRaw;
import com.likelion.backendplus4.yakplus.global.util.parser.XmlParserUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DrugProductService {

	@Value("${api.drug.product.baseUrl}")
	private String BASE_URL;

	@Value("${api.drug.product.apiKey}")
	private String API_KEY;

	private static final String DETAIL_URL = "/getDrugPrdtPrmsnDtlInq05";

	private final RestTemplate restTemplate;
	private final DrugProductRawRepository repository;



	/**
	 * 외부 API에서 데이터를 가져와서 XML 문서 데이터는 XmlParserUtil을 통해 파싱 후
	 * DrugProductRaw 엔티티로 변환(팩토리 메서드 이용)하여 DB에 저장합니다.
	 */
	@Transactional
	public void fetchAndSaveDrugProducts() {
		String apiUrl = UriComponentsBuilder.fromUriString(BASE_URL + DETAIL_URL)
			.queryParam("serviceKey", API_KEY)
			.queryParam("pageNo", 1)
			.queryParam("numOfRows", 100)
			.build(false)
			.toUriString();

		String rawXml = restTemplate.getForObject(apiUrl, String.class);
		List<DrugProductRawDto> productDtoList = XmlParserUtil.parseDrugProductsFromXml(rawXml);
		List<DrugProductRaw> drugProductRawList = productDtoList.stream()
			.map(DrugProductRaw::from)
			.toList();

		repository.saveAll(drugProductRawList);
	}
}