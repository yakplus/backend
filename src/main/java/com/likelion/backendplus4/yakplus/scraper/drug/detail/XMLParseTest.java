package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

@Component
public class XMLParseTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	public ObjectNode parseXmlToJson(String xml) throws Exception {
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		Document doc = getDocumentFromXML(xml, builder);
		Element docElement = doc.getDocumentElement();

		ObjectNode rootJson = mapper.createObjectNode();
		putDocElement(rootJson, docElement);

		// ===== 1단계: SECTION 파싱 =====
		List<SectionWrapper> sectionWrappers = new ArrayList<>();
		NodeList sectionNodes = doc.getElementsByTagName("SECTION");
		for (int i = 0; i < sectionNodes.getLength(); i++) {
			Element section = (Element) sectionNodes.item(i);
			ObjectNode sectionJson = mapper.createObjectNode();
			putDocElement(sectionJson, section);
			sectionWrappers.add(new SectionWrapper(section, sectionJson));
		}

		// ===== 2단계: ARTICLE 파싱 =====
		for (SectionWrapper wrapper : sectionWrappers) {
			NodeList articleNodes = wrapper.section.getElementsByTagName("ARTICLE");
			List<ArticleWrapper> articleWrappers = new ArrayList<>();

			for (int i = 0; i < articleNodes.getLength(); i++) {
				Element article = (Element) articleNodes.item(i);
				ObjectNode articleJson = mapper.createObjectNode();
				putDocElement(articleJson, article);
				articleWrappers.add(new ArticleWrapper(article, articleJson));
			}

			wrapper.articles = articleWrappers;
		}

		// ===== 3단계: PARAGRAPH 파싱 =====
		for (SectionWrapper wrapper : sectionWrappers) {
			for (ArticleWrapper articleWrapper : wrapper.articles) {
				NodeList paragraphNodes = articleWrapper.article.getElementsByTagName("PARAGRAPH");
				ArrayNode paragraphArray = mapper.createArrayNode();

				for (int i = 0; i < paragraphNodes.getLength(); i++) {
					String text = paragraphNodes.item(i).getTextContent().trim();
					paragraphArray.add(text);
				}

				if (paragraphArray.size() == 1) {
					articleWrapper.articleJson.set("paragraphs", paragraphArray.get(0));
				} else if (paragraphArray.size() > 1) {
					articleWrapper.articleJson.set("paragraphs", paragraphArray);
				}
			}
		}

		// ===== 4단계: JSON 조립 =====
		ArrayNode sectionsArray = mapper.createArrayNode();
		for (SectionWrapper wrapper : sectionWrappers) {
			ArrayNode articleArray = mapper.createArrayNode();

			for (ArticleWrapper articleWrapper : wrapper.articles) {
				articleArray.add(articleWrapper.articleJson);
			}

			if (articleArray.size() == 1) {
				wrapper.sectionJson.set("articles", articleArray.get(0));
			} else if (articleArray.size() > 1) {
				wrapper.sectionJson.set("articles", articleArray);
			}

			sectionsArray.add(wrapper.sectionJson);
		}

		if (sectionsArray.size() == 1) {
			rootJson.set("sections", sectionsArray.get(0));
		} else if (sectionsArray.size() > 1) {
			rootJson.set("sections", sectionsArray);
		}

		return rootJson;
	}

	// ===== 도우미 메서드 =====

	private Document getDocumentFromXML(String xml, DocumentBuilder builder) throws Exception {
		return builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
	}

	private void putDocElement(ObjectNode jsonNode, Element element) {
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attr = (Attr) attributes.item(i);
			jsonNode.put(attr.getName(), attr.getValue());
		}
	}

	// ===== 내부 구조 클래스 =====

	private static class SectionWrapper {
		public Element section;
		public ObjectNode sectionJson;
		public List<ArticleWrapper> articles = new ArrayList<>();

		public SectionWrapper(Element section, ObjectNode sectionJson) {
			this.section = section;
			this.sectionJson = sectionJson;
		}
	}

	private static class ArticleWrapper {
		public Element article;
		public ObjectNode articleJson;

		public ArticleWrapper(Element article, ObjectNode articleJson) {
			this.article = article;
			this.articleJson = articleJson;
		}
	}
}
