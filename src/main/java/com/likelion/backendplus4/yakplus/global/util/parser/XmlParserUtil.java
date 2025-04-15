package com.likelion.backendplus4.yakplus.global.util.parser;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlParserUtil {

	/**
	 * XML 문자열에서 모든 PARAGRAPH 태그 내의 텍스트를 추출합니다.
	 * @param xmlData XML 형식의 문자열
	 * @return 추출된 텍스트 (줄바꿈 문자로 구분)
	 */
	public static String extractTextFromXml(String xmlData) {
		if (xmlData == null || xmlData.trim().isEmpty()) {
			return "";
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(xmlData));
			Document doc = builder.parse(inputSource);

			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList paragraphNodes = (NodeList) xPath.evaluate("//PARAGRAPH", doc, XPathConstants.NODESET);
			StringBuilder extractedText = new StringBuilder();

			for (int i = 0; i < paragraphNodes.getLength(); i++) {
				Node node = paragraphNodes.item(i);
				String text = node.getTextContent().trim();
				if (!text.isEmpty()) {
					extractedText.append(text).append("\n");
				}
			}
			return extractedText.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<DrugProductRawDto> parseDrugProductsFromXml(String xmlData) {
		if (xmlData == null || xmlData.trim().isEmpty()) {
			log.info("없음");
			return Collections.emptyList();
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList itemNodes = (NodeList) xPath.evaluate("//item", doc, XPathConstants.NODESET);
			List<DrugProductRawDto> result = new ArrayList<>();

			for (int i = 0; i < itemNodes.getLength(); i++) {
				Element item = (Element) itemNodes.item(i);

				String itemSeq = getText(item, "ITEM_SEQ");
				String itemName = getText(item, "ITEM_NAME");
				String entpName = getText(item, "ENTP_NAME");
				String itemPermitDate = getText(item, "ITEM_PERMIT_DATE");
				String etcOtcCode = getText(item, "ETC_OTC_CODE");
				String chart = getText(item, "CHART");
				String materialName = getText(item, "MATERIAL_NAME");
				String storageMethod = getText(item, "STORAGE_METHOD");
				String validTerm = getText(item, "VALID_TERM");
				String packUnit = getText(item, "PACK_UNIT");
				String makeMaterialFlag = getText(item, "MAKE_MATERIAL_FLAG");
				String cancelName = getText(item, "CANCEL_NAME");
				String totalContent = getText(item, "TOTAL_CONTENT");

				String eeDocData = getXml(item, "EE_DOC_DATA");
				String udDocData = getXml(item, "UD_DOC_DATA");
				String nbDocData = getXml(item, "NB_DOC_DATA");
				String pnDocData = getXml(item, "PN_DOC_DATA");

				String ingrName = getText(item, "INGR_NAME");
				String rareDrugYn = getText(item, "RARE_DRUG_YN");

				DrugProductRawDto dto = new DrugProductRawDto(
					itemSeq, itemName, entpName, itemPermitDate, etcOtcCode, chart,
					materialName, storageMethod, validTerm, packUnit, makeMaterialFlag,
					cancelName, totalContent, eeDocData, udDocData, nbDocData, pnDocData,
					ingrName, rareDrugYn
				);
				result.add(dto);
			}

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static String getText(Element parent, String tag) {
		NodeList nodes = parent.getElementsByTagName(tag);
		if (nodes.getLength() > 0 && nodes.item(0) != null) {
			return nodes.item(0).getTextContent().trim();
		}
		return "";
	}

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
}
