package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.scraper.drug.ApiUriCompBuilder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrugApprovalDetailScraperService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApiUriCompBuilder apiUriCompBuilder;
    private final ApiDataDrugRepo repository;

    @Transactional
    public void getAPIData(){
        log.info("API 데이터 요청");

        String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(), String.class);
        log.debug("API Response: {}", response);

        JsonNode items = ApiResponseMapper.getItemsFromResponse(response);
        List<GovDrugDetail> drugs = toListFromJson(items);
        for (GovDrugDetail drug : drugs) {
            System.out.println(drug);
        }
        repository.saveAllAndFlush(drugs);

    }

    private List<GovDrugDetail> toListFromJson(JsonNode items) {

        log.info("items 약품 객체로 맵핑");
        try {
            List<GovDrugDetail> apiDataDrugDetails = toApiDetails(items);

            for (int i = 0; i < apiDataDrugDetails.size(); i++) {
                GovDrugDetail drugDetail = apiDataDrugDetails.get(i);
                JsonNode item = items.get(i);

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

    private List<GovDrugDetail> toApiDetails(JsonNode items) {
        try{
            return objectMapper.readValue(items.toString(),
                        new TypeReference<List<GovDrugDetail>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> getPrecau(JsonNode jsonNode) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if(!jsonNode.isNull() && jsonNode.isArray()){
            for(int j = 0; j < jsonNode.size(); j++){
                JsonNode node = jsonNode.get(j);
                result.put(node.get("title").toString(), getValueFromArrayNode(node.path("PARAGRAPH"),""));

            }
        }

        return result;
    }

    // TODO: 추후 삭제 예정
    // private String replaceText(String text){
    //     return text.replace("&#x119e; ", "&")
    //         .replace("&#x2022; ","")
    //         .replace("&#x301c; ", "~");
    // }
    private List<String> getValueFromArrayNode(JsonNode jsonNode, String key) {
        List<String> result = new ArrayList<>();
        if(!jsonNode.isNull()){
            if(jsonNode.isArray()){
                for(int j = 0; j < jsonNode.size(); j++){
                    // TODO: 추후 삭졔
                    //  result.add(replaceText(jsonNode.get(j).get(key).asText()));
                    result.add(jsonNode.get(j).get(key).asText());
                }
            } else {
                if(jsonNode.get(key)!=null){
                    result.add(jsonNode.get(key).asText());
                }

            }

        }
        return result;
    }

    private JsonNode toJsonFromXml(String usageXmlText) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        JsonNode jsonNode = xmlMapper.readTree(usageXmlText)
                                    .path("SECTION")
                                    .path("ARTICLE");
        return jsonNode;
    }


}
