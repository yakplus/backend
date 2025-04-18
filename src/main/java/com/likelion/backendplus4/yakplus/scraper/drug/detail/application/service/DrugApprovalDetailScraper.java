package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.gov.ApiUriCompBuilder;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.parser.MaterialParser;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.parser.XMLParser;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in.DrugApprovalDetailScraperUseCase;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.persistence.GovDrugDetailEntity;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.out.DrugDetailRepositoryPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrugApprovalDetailScraper implements DrugApprovalDetailScraperUseCase {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApiUriCompBuilder apiUriCompBuilder;
    private final DrugDetailRepositoryPort drugDetailRepositoryPort;



    @Transactional
    @Override
    public void requestUpdateRawData(){
        log.info("API 데이터 요청");
        String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(2), String.class);
        log.debug("API Response: {}", response);

        JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
        List<GovDrugDetailEntity> drugs = toListFromJson(items);
        for (GovDrugDetailEntity drug : drugs) {
            System.out.println(drug);
        }
        drugDetailRepositoryPort.saveAllAndFlush(drugs);
	}

    @Override
    public void requestUpdateAllRawData() {
        int totalCount= -1;
        int pageNo = 1;
        int savedCount = 0;

		do {
			String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(pageNo), String.class);
			if (totalCount == -1) {
				totalCount = ApiResponseMapper.getTotalCountFromResponse(response); // api 최초 호출 시 전체 데이터 개수 받아옴
			}
			JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
			int itemCount = items.size();
			log.info("item Count: "+ itemCount);
			List<GovDrugDetailEntity> drugs = toListFromJson(items);
			// for (GovDrugDetailEntity drug : drugs) {
			// 	System.out.println(drug);
			// }
			drugDetailRepositoryPort.saveAllAndFlush(drugs);
			pageNo++;
			savedCount += itemCount;
			log.info("Saved Count: "+ savedCount);
		} while (savedCount < totalCount);

        // log.info("api 호출 하여 개수 요청");
        // String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(), String.class);
        // log.debug("API Response: {}", response);
        // // TODO: 데이터 사이즈 가져와서 반복문
        // JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
        // List<GovDrugDetailEntity> drugs = toListFromJson(items);
        // for (GovDrugDetailEntity drug : drugs) {
        //     System.out.println(drug);
        // }
        // drugDetailRepositoryPort.saveAllAndFlush(drugs);
    }

    private List<GovDrugDetailEntity> toListFromJson(JsonNode items) {

        log.info("items 약품 객체로 맵핑");
        try {
            List<GovDrugDetailEntity> apiDataDrugDetails = toApiDetails(items);

            for (int i = 0; i < apiDataDrugDetails.size(); i++) {
                GovDrugDetailEntity drugDetail = apiDataDrugDetails.get(i);
                JsonNode item = items.get(i);
				// TODO: item id 로깅 필요
				log.info("item seq: "+ item.get("ITEM_SEQ").asText());

                String materialRawData = item.get("MATERIAL_NAME").asText();
                String materialInfo = MaterialParser.parseMaterial(materialRawData);
                drugDetail.changeMaterialInfo(materialInfo);

                String efficacyXmlText = item.get("EE_DOC_DATA").asText();
                String efficacy = XMLParser.toJson(efficacyXmlText);
                drugDetail.changeEfficacy(efficacy);

                String usageXmlText = items.get(i).get("UD_DOC_DATA").asText();
                String usages = XMLParser.toJson(usageXmlText);
                drugDetail.changeUsage(usages);

                String precautionxmlText = items.get(i).get("NB_DOC_DATA").asText();
                String precautions = XMLParser.toJson(precautionxmlText);
                drugDetail.changePrecaution(precautions);
            }

            return apiDataDrugDetails;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private List<GovDrugDetailEntity> toApiDetails(JsonNode items) {
        try{
            return objectMapper.readValue(items.toString(),
                        new TypeReference<List<GovDrugDetailEntity>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // private JsonNode toJsonFromXml(String usageXmlText) throws JsonProcessingException {
    //     XmlMapper xmlMapper = new XmlMapper();
    //
    //     JsonNode jsonNode = xmlMapper.readTree(usageXmlText)
    //                                 .path("SECTION")
    //                                 .path("ARTICLE");
    //     return jsonNode;
    // }

    // TODO: 추후 삭제 예정
    // private String replaceText(String text){
    //     return text.replace("&#x119e; ", "&")
    //         .replace("&#x2022; ","")
    //         .replace("&#x301c; ", "~");
    // }

}
