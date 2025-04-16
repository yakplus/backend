package com.likelion.backendplus4.yakplus.global.util.parser;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.likelion.backendplus4.yakplus.domain.drug.dto.DrugProductRawDto;
import com.likelion.backendplus4.yakplus.domain.drug.entity.Ingredient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlParserUtil {

	/**
	 * XML 문자열에서 모든 PARAGRAPH 태그 내의 텍스트를 추출합니다.
	 * 각 PARAGRAPH 태그의 텍스트를 줄바꿈 문자로 구분하여 합칩니다.
	 *
	 * @param xmlData XML 형식의 문자열
	 * @return 추출된 텍스트 (줄바꿈 문자로 구분)
	 */
	public static String extractTextFromXml(String xmlData) {
		if (xmlData == null || xmlData.trim().isEmpty()) {
			return "";
		}
		try {
			// DocumentBuilderFactory를 생성하여 XML 파싱에 필요한 DocumentBuilder 획득
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// XML 문자열을 InputSource로 감싸고 파싱함
			InputSource inputSource = new InputSource(new StringReader(xmlData));
			Document doc = builder.parse(inputSource);

			// XPath를 사용하여 모든 PARAGRAPH 태그 노드를 선택
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList paragraphNodes = (NodeList) xPath.evaluate("//PARAGRAPH", doc, XPathConstants.NODESET);
			StringBuilder extractedText = new StringBuilder();

			// 각 PARAGRAPH 노드의 텍스트를 추출하고 줄바꿈을 추가
			for (int i = 0; i < paragraphNodes.getLength(); i++) {
				Node node = paragraphNodes.item(i);
				String text = node.getTextContent().trim();
				if (!text.isEmpty()) {
					extractedText.append(text).append("\n");
				}
			}
			return extractedText.toString().trim();
		} catch (Exception e) {
			// 예외 발생 시 스택 트레이스 출력 후 빈 문자열 리턴
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 주어진 XML 문자열 내의 <item> 태그들을 파싱하여 DrugProductRawDto 객체 리스트로 반환합니다.
	 * 각 <item> 태그 하위의 데이터를 추출하여 DTO 생성 시,
	 * MATERIAL_NAME 태그의 값은 성분(Ingredient) 리스트로 파싱합니다.
	 *
	 * @param xmlData XML 형식의 문자열 (API 응답 데이터)
	 * @return DrugProductRawDto 객체의 리스트, XML 데이터가 없으면 빈 리스트 반환
	 */
	public static List<DrugProductRawDto> parseDrugProductsFromXml(String xmlData) {
		if (xmlData == null || xmlData.trim().isEmpty()) {
			log.info("없음");
			return Collections.emptyList();
		}

		try {
			// XML 파싱을 위한 DocumentBuilder 생성
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

			// XPath를 사용하여 모든 <item> 노드를 선택
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList itemNodes = (NodeList) xPath.evaluate("//item", doc, XPathConstants.NODESET);
			List<DrugProductRawDto> result = new ArrayList<>();

			// 각 <item> 노드를 순회하며 데이터를 추출함
			for (int i = 0; i < itemNodes.getLength(); i++) {
				Element item = (Element) itemNodes.item(i);

				// 각 태그의 텍스트 값 추출
				String itemSeq = getText(item, "ITEM_SEQ");
				String itemName = getText(item, "ITEM_NAME");
				String entpName = getText(item, "ENTP_NAME");
				String itemPermitDate = getText(item, "ITEM_PERMIT_DATE");
				String etcOtcCode = getText(item, "ETC_OTC_CODE");
				String chart = getText(item, "CHART");
				// MATERIAL_NAME 태그를 파싱하여 성분(Ingredient) 리스트로 변환
				List<Ingredient> ingredientList = parseIngredients(getText(item, "MATERIAL_NAME"));
				String storageMethod = getText(item, "STORAGE_METHOD");
				String validTerm = getText(item, "VALID_TERM");
				String packUnit = getText(item, "PACK_UNIT");
				String makeMaterialFlag = getText(item, "MAKE_MATERIAL_FLAG");
				String cancelName = getText(item, "CANCEL_NAME");
				String totalContent = getText(item, "TOTAL_CONTENT");

				// EE/UD/NB/PN_DOC_DATA 태그를 XML 형식 그대로 추출
				String eeDocData = getXml(item, "EE_DOC_DATA");
				String udDocData = getXml(item, "UD_DOC_DATA");
				String nbDocData = getXml(item, "NB_DOC_DATA");
				String pnDocData = getXml(item, "PN_DOC_DATA");

				// 기타 태그 값 추출
				String ingrName = getText(item, "INGR_NAME");
				String rareDrugYn = getText(item, "RARE_DRUG_YN");

				// DTO 생성 (여기서 ingredientList를 포함해서 생성)
				DrugProductRawDto dto = new DrugProductRawDto(
					itemSeq, itemName, entpName, itemPermitDate, etcOtcCode, chart,
					ingredientList, storageMethod, validTerm, packUnit, makeMaterialFlag,
					cancelName, totalContent, eeDocData, udDocData, nbDocData, pnDocData,
					ingrName, rareDrugYn
				);
				result.add(dto);
			}

			return result;

		} catch (Exception e) {
			// 예외 발생 시 스택 트레이스 출력 후 빈 리스트 반환
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * 주어진 부모 엘리먼트에서 특정 태그 이름에 해당하는 노드의 텍스트를 반환합니다.
	 *
	 * @param parent 부모 Element
	 * @param tag    찾을 태그 이름
	 * @return 해당 태그의 텍스트 값, 없으면 빈 문자열
	 */
	private static String getText(Element parent, String tag) {
		NodeList nodes = parent.getElementsByTagName(tag);
		if (nodes.getLength() > 0 && nodes.item(0) != null) {
			return nodes.item(0).getTextContent().trim();
		}
		return "";
	}

	/**
	 * 주어진 부모 엘리먼트에서 특정 태그 이름에 해당하는 노드 전체(태그 포함)를 XML 문자열로 변환하여 반환합니다.
	 * 이 방법은 내부 XML 구조나 CDATA 내용 등을 그대로 보존할 때 유용합니다.
	 *
	 * @param parent 부모 Element
	 * @param tag    찾을 태그 이름
	 * @return 해당 태그의 XML 문자열, 없으면 빈 문자열
	 */
	private static String getXml(Element parent, String tag) {
		NodeList nodes = parent.getElementsByTagName(tag);
		if (nodes.getLength() > 0 && nodes.item(0) != null) {
			Node node = nodes.item(0);
			StringWriter sw = new StringWriter();
			try {
				Transformer t = TransformerFactory.newInstance().newTransformer();
				t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				t.setOutputProperty(OutputKeys.INDENT, "no");
				t.transform(new DOMSource(node), new StreamResult(sw));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
			return sw.toString();
		}
		return "";
	}

	/**
	 * 성분 문자열(raw MATERIAL_NAME) 데이터를 파싱하여 Ingredient 객체 리스트로 변환합니다.
	 * 각 성분은 세미콜론(;)으로 구분되며, 각 성분 항목은 파이프(|)로 필드가 구분됩니다.
	 * 각 필드는 "키 : 값" 형태로 구성됩니다.
	 *
	 * 예시 문자열:
	 *   "총량 : 100밀리리터 중|성분명 : 포도당|분량 : 10|단위 : 그램|규격 : KP|성분정보 : |비고 :"
	 *
	 * @param raw MATERIAL_NAME 태그의 원시 문자열
	 * @return 파싱된 Ingredient 객체의 리스트, 데이터가 없으면 빈 리스트 반환
	 */
	public static List<Ingredient> parseIngredients(String raw) {
		if (raw == null || raw.isBlank()) return List.of();

		// 세미콜론으로 성분 항목 분리
		String[] parts = raw.split(";");
		List<Ingredient> result = new ArrayList<>();

		// 각 성분 항목 파싱
		for (String part : parts) {
			// 파이프(|) 기호로 필드 분리
			String[] fields = part.split("\\|");
			Map<String, String> map = new HashMap<>();
			// 각 필드를 "키 : 값"으로 분리하여 맵에 저장
			for (String field : fields) {
				String[] keyValue = field.split(":", 2);
				if (keyValue.length == 2) {
					map.put(keyValue[0].trim(), keyValue[1].trim());
				}
			}

			// Ingredient 객체 생성 후 맵에서 값 설정
			Ingredient ingredient = new Ingredient();
			ingredient.setTotalContent(map.getOrDefault("총량", ""));
			ingredient.setName(map.getOrDefault("성분명", ""));
			ingredient.setAmount(map.getOrDefault("분량", ""));
			ingredient.setUnit(map.getOrDefault("단위", ""));
			ingredient.setStandard(map.getOrDefault("규격", ""));
			ingredient.setInfo(map.getOrDefault("성분정보", ""));
			ingredient.setRemark(map.getOrDefault("비고", ""));
			result.add(ingredient);
		}

		return result;
	}
}