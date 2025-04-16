package com.likelion.backendplus4.yakplus.application.mapper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.application.dto.out.persistance.MedicineDetailDto;
import com.likelion.backendplus4.yakplus.domain.entity.Medicine;

public class MedicineDetailEntityMapper {

	private static final ObjectMapper mapper = new ObjectMapper();
	public static Medicine toEntity(MedicineDetailDto dto) {
		return Medicine.builder()
			.itemSeq(dto.getItemSeq())
			.itemName(dto.getItemName())
			.entpName(dto.getEntpName())
			.itemPermitDate(dto.getItemPermitDate()) // LocalDate 그대로 전달
			.etcOtcCode(dto.getEtcOtcCode())
			.chart(dto.getChart())
			.materialName(extractMaterialInfo(dto.getMaterialName())) // 원료성분 => |단위로 끊어서 json 형태의 string으로 저장
			.storageMethod(dto.getStorageMethod())
			.validTerm(dto.getValidTerm())
			.packUnit(dto.getPackUnit())
			.makeMaterialFlag(dto.getMakeMaterialFlag())
			.cancelName(dto.getCancelName())
			.totalContent(dto.getTotalContent())
			.efficacy(extractXmlText(dto.getEeDocData()))
			.medicationUsage(extractXmlText(dto.getUdDocData()))
			.precautionGeneral(extractXmlText(dto.getNbDocData()))
			.precautionSpecial(extractXmlText(dto.getPnDocData()))
			.ingrName(dto.getIngrName())
			.rareMedicineYn(dto.getRareDrugYn())
			.build();
	}

	private static String extractMaterialInfo(String materialInfo) {
		if (materialInfo == null || materialInfo.isBlank()) {
			return "[]";
		}
		try {
			String[] parts = materialInfo.split("\\|");
			Map<String, String> result = new LinkedHashMap<>();

			for (String part : parts) {
				String trimmed = part.trim(); // 공백 제거
				if (trimmed.isBlank()) {     // 빈 문자열 제외
					continue;
				}
				String[] keyValue = trimmed.split(":", 2);
				String key = keyValue[0].trim();
				String value = (keyValue.length > 1) ? keyValue[1].trim() : ""; // 조건 1: 길이체크 해서 key 값에 해당하는 value 없으면 공백으로 처리

				if (key.equals("총량") && value.endsWith("중")) { //  조건 2: "총량" key일 경우, 마지막이 "중"이면 제거
					value = value.substring(0, value.length() - 1).trim();
				}

				result.put(key, value);

			}

			return mapper.writeValueAsString(result); // JSON 문자열 반환
		} catch (Exception e) {
			System.err.println(">>> JSON Convert Error: " + e.getMessage());
			return "[]";
		}

	}

	// CDATA 포함된 XML 텍스트에서 <PARAGRAPH> 내용만 추출해서 이어 붙임
	// Article의 title에만 내용이 있는 경우도 있음...
	// 1. article의 title과 paragraph 에 내용이 둘 다 있는 경우, paragraph 속 데이터만 가져옴
	// 2. article의 title에만 내용이 있는 경우, title 속 데이터만 가져옴
	private static String extractXmlText(String xml) {
		if (xml == null || xml.isBlank()) {
			return "[]";
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

			NodeList articleNodes = doc.getElementsByTagName("ARTICLE");
			List<String> contents = new ArrayList<>();

			for (int i = 0; i < articleNodes.getLength(); i++) {
				Element article = (Element) articleNodes.item(i);
				// title 추출
				String title =  cleanText(article.getAttribute("title").trim());
				if(!title.isBlank()) {
					contents.add(title);
				}
				// paragraph 추출
				NodeList paragraphs = article.getElementsByTagName("PARAGRAPH");
				for (int j = 0; j < paragraphs.getLength(); j++) {
					String text = cleanText(paragraphs.item(j).getTextContent().trim());
					if (!text.isBlank()) {
						contents.add(text);
					}
				}
			}

			return mapper.writeValueAsString(contents);
		} catch (Exception e) {
			System.err.println(">>> Falied to Parse XML: " + e.getMessage());
			return "[]";
		}
	}

	private static String cleanText(String text) {
		if (text == null) return "";
		return text
			.replaceAll("^[○\\-•・▶▷◆️\\s]*", "")  // 앞쪽 특수문자 제거
			.replaceAll("[\\r\\n]+", " ")           // 개행 제거
			.replaceAll("\\s{2,}", " ")             // 과도한 공백 제거
			.trim();
	}

}
