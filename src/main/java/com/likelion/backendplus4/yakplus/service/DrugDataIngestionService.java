package com.likelion.backendplus4.yakplus.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.domain.DrugCombinedInfo;
import com.likelion.backendplus4.yakplus.domain.DrugImageInfo;
import com.likelion.backendplus4.yakplus.domain.DrugPermitInfo;
import com.likelion.backendplus4.yakplus.exception.DataIngestionException;
import com.likelion.backendplus4.yakplus.port.DrugCombinedInfoPort;
import com.likelion.backendplus4.yakplus.port.DrugImageInfoPort;
import com.likelion.backendplus4.yakplus.port.DrugPermitInfoPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DrugDataIngestionService는 외부 API를 호출하여 의약품 허가정보와 이미지 정보를 수집하고,
 * 이를 DrugPermitInfo, DrugImageInfo, DrugCombinedInfo 도메인에 저장하는 역할을 수행한다.
 * [동작 원리]
 * 1. ingestPermitInfo() 메서드는 외부 Permit API를 호출하여 JSON 응답을 받고,
 * 응답의 body > items 배열에 대해 각 항목을 DrugPermitInfo 객체로 매핑한다.
 * 그 후, DrugPermitInfoPort를 통해 데이터베이스에 저장하고, 동일한 정보를 DrugCombinedInfo에 (이미지 정보 null) 저장한다.
 * 2. ingestImageInfo() 메서드는 저장된 permit 데이터의 item_seq 목록을 조회하여,
 * 각 item_seq마다 외부 이미지 API를 호출해 BIG_PRDT_IMG_URL 정보를 추출한다.
 * DrugImageInfoPort를 통해 이미지 정보를 저장하고, DrugCombinedInfoPort를 통해 해당 항목의 이미지 URL을 업데이트한다.
 *
 * @author 정안식
 * @since 2025-04-14 최초 작성
 */
// TODO: @Transactional 처리 해야함, 전역 예외 처리 필요
@Service
@RequiredArgsConstructor
public class DrugDataIngestionService {

    private static final Logger logger = LoggerFactory.getLogger(DrugDataIngestionService.class);

    private final DrugPermitInfoPort permitPort;
    private final DrugImageInfoPort imagePort;
    private final DrugCombinedInfoPort combinedPort;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 외부 API 설정값 (application.properties 또는 환경변수에서 주입)
    @Value("${DRUG_API_PERMIT_URL}")
    private String permitApiUrl;

    @Value("${DRUG_API_IMAGE_URL}")
    private String imageApiUrl;

    @Value("${DRUG_API_SERVICE_KEY}")
    private String serviceKey;

    /**
     * 외부 API를 호출하여 의약품 허가정보를 수집하고 저장한다.
     * 프로세스 순서:
     * 1. 외부 Permit API를 호출하여 JSON 응답을 받는다.
     * 2. 응답 JSON에서 body > items 배열을 추출한다.
     * 3. 각 JSON 항목을 DrugPermitInfo 도메인 객체로 매핑한다.
     * 4. DrugPermitInfoPort로 permit 정보를 저장한다.
     * 5. DrugPermitInfo 정보를 기반으로 이미지 정보가 없는 DrugCombinedInfo 객체를 생성하여 저장한다.
     *
     * @throws DataIngestionException API 호출 또는 JSON 파싱 중 예외 발생 시 예외를 던진다.
     */
    public void ingestPermitInfo() {
        try {
            String url = String.format("%s?serviceKey=%s&pageNo=1&numOfRows=100&type=json", permitApiUrl, serviceKey);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("Permit API Response: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("body").path("items");

            if (items.isArray()) {
                for (JsonNode node : items) {
                    DrugPermitInfo permitInfo = mapJsonNodeToDrugPermitInfo(node);
                    permitPort.save(permitInfo);

                    // 초기에는 이미지 정보가 없으므로 null 처리
                    DrugCombinedInfo combinedInfo = mapToDrugCombinedInfo(permitInfo, null);
                    combinedPort.save(combinedInfo);
                }
            } else {
                logger.warn("Permit API 응답에 items가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            logger.error("Permit 정보 수집에 실패하였습니다.", e);
            throw new DataIngestionException("Permit 정보 수집 실패", e);
        }
    }

    /**
     * 저장된 모든 item_seq에 대해 외부 이미지 API를 호출하여 이미지 정보를 수집하고,
     * DrugImageInfo와 DrugCombinedInfo 도메인 정보를 업데이트한다.
     * 프로세스 순서:
     * 1. DrugPermitInfoPort를 통해 저장된 item_seq 목록을 조회한다.
     * 2. 각 item_seq마다 외부 이미지 API를 호출하여 BIG_PRDT_IMG_URL 정보를 추출한다.
     * 3. 추출한 이미지 정보를 DrugImageInfoPort로 저장한다.
     * 4. DrugCombinedInfoPort를 통해 해당 item_seq에 대한 결합 데이터의 이미지 URL을 업데이트한다.
     *
     * @throws DataIngestionException 이미지 정보 수집 및 업데이트 과정 중 예외 발생 시 예외를 던진다.
     */
    public void ingestImageInfo() {
        try {
            List<String> itemSeqList = permitPort.findAllItemSeqs();
            for (String itemSeq : itemSeqList) {
                try {
                    String url = String.format("%s?serviceKey=%s&item_seq=%s&type=json", imageApiUrl, serviceKey, itemSeq);
                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                    logger.info("Image API Response for itemSeq {}: {}", itemSeq, response.getBody());

                    JsonNode root = objectMapper.readTree(response.getBody());
                    JsonNode items = root.path("body").path("items");

                    if (items.isArray() && items.size() > 0) {
                        String bigPrdtImgUrl = items.get(0).path("BIG_PRDT_IMG_URL").asText();

                        // DrugImageInfo 저장
                        imagePort.save(DrugImageInfo.builder()
                                .itemSeq(itemSeq)
                                .bigPrdtImgUrl(bigPrdtImgUrl)
                                .build());

                        // DrugCombinedInfo 업데이트
                        combinedPort.findById(itemSeq).ifPresentOrElse(
                                combinedInfo -> {
                                    combinedInfo.setBigPrdtImgUrl(bigPrdtImgUrl);
                                    combinedPort.save(combinedInfo);
                                },
                                () -> logger.warn("Combined info not found for ITEM_SEQ: {}", itemSeq)
                        );
                    } else {
                        logger.warn("Image API 응답에 itemSeq {}에 해당하는 데이터가 없습니다.", itemSeq);
                    }
                } catch (Exception ex) {
                    // FIXME: 개별 item_seq 처리 시 예외가 발생하면 추가 검토 후 수정 필요
                    logger.error("ItemSeq {}에 대한 이미지 정보 수집 실패", itemSeq, ex);
                }
            }
        } catch (Exception e) {
            logger.error("이미지 정보 수집에 실패하였습니다.", e);
            throw new DataIngestionException("이미지 정보 수집 실패", e);
        }
    }

    /**
     * JSON 응답의 단일 항목을 DrugPermitInfo 도메인 객체로 매핑한다.
     * MATERIAL_NAME과 _DATA 필드는 별도 파싱하여 구조화된 JSON 문자열로 변환한다.
     *
     * @param node JSON 노드
     * @return DrugPermitInfo 객체
     */
    private DrugPermitInfo mapJsonNodeToDrugPermitInfo(JsonNode node) {
        return DrugPermitInfo.builder()
                .itemSeq(node.path("ITEM_SEQ").asText())
                .itemName(node.path("ITEM_NAME").asText())
                .entpName(node.path("ENTP_NAME").asText())
                .itemPermitDate(LocalDate.parse(node.path("ITEM_PERMIT_DATE").asText(), dateFormatter))
                .etcOtcCode(node.path("ETC_OTC_CODE").asText())
                .chart(node.path("CHART").asText())
                // MATERIAL_NAME를 구조화된 JSON 문자열로 변환
                .materialName(parseMaterialName(node.path("MATERIAL_NAME").asText()))
                .storageMethod(node.path("STORAGE_METHOD").asText())
                .validTerm(node.path("VALID_TERM").asText())
                .packUnit(node.path("PACK_UNIT").asText())
                .makeMaterialFlag(node.path("MAKE_MATERIAL_FLAG").asText())
                .cancelName(node.path("CANCEL_NAME").asText())
                .totalContent(node.path("TOTAL_CONTENT").asText())
                // _DATA 필드들은 HTML 태그를 제거하고 한글만 추출하여 구조화된 JSON 배열로 저장
                .eeDocData(parseDataField(node.path("EE_DOC_DATA").asText()))
                .udDocData(parseDataField(node.path("UD_DOC_DATA").asText()))
                .nbDocData(parseDataField(node.path("NB_DOC_DATA").asText()))
                .pnDocData(parseDataField(node.path("PN_DOC_DATA").asText()))
                .ingrName(node.path("INGR_NAME").asText())
                .rareDrugYn(node.path("RARE_DRUG_YN").asText())
                .build();
    }

    /**
     * MATERIAL_NAME 필드의 raw 문자열을 파싱하여 구조화된 JSON 문자열로 변환한다.
     * 프로세스:
     * 1. 입력 문자열이 null 또는 빈 문자열이면 "[]"를 반환한다.
     * 2. 세미콜론(";")을 기준으로 그룹을 나누고, 각 그룹 내에서 파이프("|")와 콜론(":")을 기준으로 key-value 쌍으로 분리한다.
     * 3. 추출된 각 그룹을 Map 객체에 저장한 후, 모든 Map을 List에 담고 JSON 배열로 변환하여 반환한다.
     *
     * @param rawMaterialName 원시 MATERIAL_NAME 문자열
     * @return 구조화된 JSON 배열 문자열
     */
    private String parseMaterialName(String rawMaterialName) {
        if (rawMaterialName == null || rawMaterialName.trim().isEmpty()) {
            return "[]";
        }
        String[] groups = rawMaterialName.split(";");
        List<Map<String, String>> result = new ArrayList<>();
        for (String group : groups) {
            group = group.trim();
            if (group.isEmpty()) continue;
            Map<String, String> component = new HashMap<>();
            String[] parts = group.split("\\|");
            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    component.put(key, value);
                }
            }
            if (!component.isEmpty()) {
                result.add(component);
            }
        }
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("MaterialName 파싱 오류", e);
            return "[]";
        }
    }

    /**
     * _DATA로 끝나는 필드(EE_DOC_DATA, UD_DOC_DATA, NB_DOC_DATA, PN_DOC_DATA)의 raw 문자열에서
     * CDATA 섹션 내부의 텍스트를 추출한 후, 괄호와 그 내부의 내용을 제거하여 JSON 배열 형태의 문자열로 변환한다.
     * 프로세스:
     * 1. 입력 문자열이 null 또는 빈 문자열이면 "[]"를 반환한다.
     * 2. 정규표현식을 사용해 CDATA 섹션 내부의 내용을 추출한다.
     * 3. 추출된 내용에서 괄호와 그 내부의 텍스트 (예: "(단위: mL)")를 제거한다.
     * 4. 추출된 값들이 있으면 리스트에 추가한다.
     * 5. 리스트를 JSON 배열 문자열로 변환하여 반환한다.
     *
     * @param raw 원시 _DATA 문자열
     * @return 괄호 및 그 내부의 내용을 제거한 텍스트들의 JSON 배열 문자열
     */
    private String parseDataField(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "[]";
        }
        List<String> results = new ArrayList<>();
        // CDATA 섹션 추출 (DOTALL 플래그로 여러 줄 처리)
        Pattern cdataPattern = Pattern.compile("<!\\[CDATA\\[(.*?)\\]\\]>", Pattern.DOTALL);
        Matcher cdataMatcher = cdataPattern.matcher(raw);
        while (cdataMatcher.find()) {
            String content = cdataMatcher.group(1).trim();
            if (!content.isEmpty()) {
                // HTML 엔티티 (&nbsp;, &#x2022; 등) 제거
                content = content.replaceAll("&[^;]+;", "").trim();
                // 괄호와 그 내부의 내용을 제거 (예: (단위: mL))
                content = content.replaceAll("\\(.*?\\)", "").trim();
                if (!content.isEmpty()) {
                    results.add(content);
                }
            }
        }
        if (results.isEmpty()) {
            try {
                raw = raw.trim(); // 프롤로그 오류 방지용
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(raw)));

                NodeList articles = doc.getElementsByTagName("ARTICLE");
                for (int i = 0; i < articles.getLength(); i++) {
                    Element article = (Element) articles.item(i);
                    String title = article.getAttribute("title").trim();
                    if (!title.isEmpty()) results.add(title);
                }
            } catch (Exception e) {
                logger.error("XML 파싱 실패", e);
            }
        }
        try {
            return objectMapper.writeValueAsString(results);
        } catch (Exception e) {
            logger.error("데이터 필드 파싱 오류", e);
            return "[]";
        }
    }

    /**
     * DrugPermitInfo 객체와 이미지 URL을 사용하여 DrugCombinedInfo 도메인 객체로 매핑한다.
     * 프로세스:
     * 1. DrugPermitInfo 객체의 모든 필드를 DrugCombinedInfo 빌더에 복사한다.
     * 2. 이미지 URL을 설정하고 DrugCombinedInfo 객체를 반환한다.
     *
     * @param permitInfo    DrugPermitInfo 객체
     * @param bigPrdtImgUrl 이미지 URL (수집되지 않은 경우 null)
     * @return DrugCombinedInfo 객체
     */
    private DrugCombinedInfo mapToDrugCombinedInfo(DrugPermitInfo permitInfo, String bigPrdtImgUrl) {
        return DrugCombinedInfo.builder()
                .itemSeq(permitInfo.getItemSeq())
                .itemName(permitInfo.getItemName())
                .entpName(permitInfo.getEntpName())
                .itemPermitDate(permitInfo.getItemPermitDate())
                .etcOtcCode(permitInfo.getEtcOtcCode())
                .chart(permitInfo.getChart())
                .materialName(permitInfo.getMaterialName())
                .storageMethod(permitInfo.getStorageMethod())
                .validTerm(permitInfo.getValidTerm())
                .packUnit(permitInfo.getPackUnit())
                .makeMaterialFlag(permitInfo.getMakeMaterialFlag())
                .cancelName(permitInfo.getCancelName())
                .totalContent(permitInfo.getTotalContent())
                .eeDocData(permitInfo.getEeDocData())
                .udDocData(permitInfo.getUdDocData())
                .nbDocData(permitInfo.getNbDocData())
                .pnDocData(permitInfo.getPnDocData())
                .ingrName(permitInfo.getIngrName())
                .rareDrugYn(permitInfo.getRareDrugYn())
                .bigPrdtImgUrl(bigPrdtImgUrl)
                .build();
    }
}
