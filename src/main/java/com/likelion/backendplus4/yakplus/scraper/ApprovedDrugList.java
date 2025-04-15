package com.likelion.backendplus4.yakplus.scraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
public class ApprovedDrugList {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ApiUriCompBuilder apiUriCompBuilder;
    private final ApiDataDrungRepo  repository;

    @Transactional
    public void getAPIData(){
        log.info("API 데이터 요청");
        String response = restTemplate.getForObject(apiUriCompBuilder.getUriForDetailApi(), String.class);
        log.debug("API Response: {}", response);

        JsonNode items = getItemsFromResponse(response);
        List<ApiDataDrugDetail> drugs = toListFromJson(items);
        repository.saveAllAndFlush(drugs);

    }

    private List<ApiDataDrugDetail> toListFromJson(JsonNode items) {

        log.info("items 약품 객체로 맵핑");
        try {
            List<ApiDataDrugDetail> apiDataDrugDetails = objectMapper.readValue(items.toString(),
                                                        new TypeReference<List<ApiDataDrugDetail>>() {
                                                        });


            for (int i = 0; i < apiDataDrugDetails.size(); i++) {
                ApiDataDrugDetail drug = apiDataDrugDetails.get(i);
                try {
                    String usageXmlText = items.get(i).get("UD_DOC_DATA").asText();
                    JsonNode jsonNode = toJsonFromXml(usageXmlText).path("PARAGRAPH");
                    List<String> usages = getValueFromArrayNode(jsonNode,"");
                    drug.setDrugUsage(usages);
                } catch (JsonProcessingException e) {
                    log.error("용법 추출 실패");
                    throw new RuntimeException(e);
                }

                try {
                    String xmlText = items.get(i).get("NB_DOC_DATA").asText();
                    JsonNode jsonNode = toJsonFromXml(xmlText);
                    // List<String> precautions = getValueFromArrayNode(jsonNode,"title");
                    drug.setPrecautions(getPrecau(jsonNode));
                } catch (JsonProcessingException e) {
                    log.error("주의 사항 실패");
                    throw new RuntimeException(e);
                }
            }

            return apiDataDrugDetails;
        } catch (JsonProcessingException e) {
            log.error("items JSON 처리 실패");
            //TODO: CustomException 만들고, ControllerAdvice로 예외처리 필요
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
    private String replaceText(String text){
        return text.replace("&#x119e; ", "&")
            .replace("&#x2022; ","")
            .replace("&#x301c; ", "~");
    }
    private List<String> getValueFromArrayNode(JsonNode jsonNode, String key) {
        System.out.println("jsonNode = " + jsonNode);
        System.out.println("key = " + key);
        List<String> result = new ArrayList<>();
        if(!jsonNode.isNull()){
            if(jsonNode.isArray()){
                for(int j = 0; j < jsonNode.size(); j++){
                    result.add(replaceText(jsonNode.get(j).get(key).asText()));
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

    private JsonNode getItemsFromResponse(String response) {
        log.info("응답에서 items 값 추출");
        try {
            return objectMapper.readTree(response)
                    .path("body")
                    .path("items");
        } catch (JsonProcessingException e) {
            log.error("items 추출 실패");
            //TODO: CustomException 만들고, ControllerAdvice로 예외처리 필요
            throw new RuntimeException(e);
        }
    }
}
