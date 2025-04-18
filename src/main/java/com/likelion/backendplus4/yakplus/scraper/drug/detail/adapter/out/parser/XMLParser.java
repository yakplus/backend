package com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.out.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XMLParser {
	public static String toJson(String xml) {
		// TODO: xml 태그 null 체크 해서 없는건 빈 객체 리턴
		System.out.println("xml = " + xml);
		if(xml == "null" || xml==null || xml.trim().isEmpty()) {
			return "{\"\": \"\"}";
		}

		Document doc = parseXmlString(xml);
		Element root = doc.getDocumentElement();

		List<SectionTag> allSections = new ArrayList<>();
		List<ArticleTag> allArticles = new ArrayList<>();
		List<ParagraphTag> allParagraphs = new ArrayList<>();

		Map<Element, SectionTag> sectionMap = new HashMap<>();
		Map<Element, ArticleTag> articleMap = new HashMap<>();

		DocTag docTag = new DocTag(root, allSections);
		parseSesctions(root, allSections, sectionMap);
		parseArticles(root, allArticles, articleMap, sectionMap);
		parseParagraph(root, allParagraphs, articleMap);
		return convertJson(docTag);
	}
	private static final ObjectMapper mapper = new ObjectMapper();

	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	private static String convertJson(DocTag docTag) {
		try {
			return mapper.writeValueAsString(docTag);
			//TODO: 예외처리 후 삭제
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static void parseParagraph(Element root, List<ParagraphTag> allParagraphs, Map<Element, ArticleTag> articleMap) {
		NodeList paraNodes = root.getElementsByTagName("PARAGRAPH");
		
		if(paraNodes.getLength() != 0){
			for (int i = 0; i < paraNodes.getLength(); i++) {
				Element paragraphElement  = (Element) paraNodes.item(i);
				ParagraphTag paragraphTag = new ParagraphTag();
				paragraphTag.tagName      = paragraphElement.getAttribute("tagName");
				paragraphTag.textIndent   = paragraphElement.getAttribute("textIndent");
				paragraphTag.marginLeft   = paragraphElement.getAttribute("marginLeft");
				paragraphTag.text         = paragraphElement.getTextContent().trim();

				allParagraphs.add(paragraphTag);
				mapSectionFromArticle(articleMap, paragraphTag, paragraphElement);
			}
		}

	}

	private static void parseArticles(Element root, List<ArticleTag> allArticles,
									Map<Element, ArticleTag> articleMap,
									Map<Element, SectionTag> sectionMap) {
		NodeList artNodes = root.getElementsByTagName("ARTICLE");
		if(artNodes.getLength() > 0) {
			for (int i = 0; i < artNodes.getLength(); i++) {
				Element artElement = (Element) artNodes.item(i);
				ArticleTag articleTag  = new ArticleTag();
				articleTag.title       = artElement.getAttribute("title");
				articleTag.paragraphs  = new ArrayList<>();

				allArticles.add(articleTag);
				articleMap.put(artElement, articleTag);
				mapSectionFromArticle(sectionMap, articleTag, artElement);
			}
		}

	}

	private static void mapSectionFromArticle(Map<Element, ? extends Tags>  map, Tags tags, Element element) {
		Element parentElement = (Element) element.getParentNode();
		Tags parentTag = map.get(parentElement);
		if (parentTag != null) {
			parentTag.addTag(tags);
		}
	}

	private static void parseSesctions(Element root, List<SectionTag> allSections, Map<Element, SectionTag> sectionMap) {
		NodeList secNodes = root.getElementsByTagName("SECTION");

		if(secNodes.getLength() > 0) {
			for (int i = 0; i < secNodes.getLength(); i++) {
				Element secEl     = (Element) secNodes.item(i);
				SectionTag secDto = new SectionTag();
				secDto.title      = secEl.getAttribute("title");
				secDto.articles   = new ArrayList<>();

				allSections.add(secDto);
				sectionMap.put(secEl, secDto);
			}
		}
	}

	private static Document parseXmlString(String xml) {
		//TODO: 예외처리 후 삭제
		try {
			return documentBuilderFactory.newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			// System.out.println(xml);
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			//TODO DocumentBulider 생성 실패
			throw new RuntimeException(e);
		}
	}

	private static class DocTag implements Tags {
		public String title;
		public String type;
		public List<SectionTag> sections;

		DocTag(Element root, List sections) {
			this.title    = root.getAttribute("title");
			this.type     = root.getAttribute("type");
			this.sections = sections;
		}

		@Override
		public void addTag(Tags tags) {
			sections.add((SectionTag) tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof DocTag;
		}
	}

	private static class SectionTag implements Tags {
		public String title;
		public List<ArticleTag> articles;

		@Override
		public void addTag(Tags tags) {
			articles.add((ArticleTag)tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof SectionTag;
		}
	}

	private static class ArticleTag implements Tags {
		public String title;
		public List<ParagraphTag> paragraphs;

		@Override
		public void addTag(Tags tags) {
			paragraphs.add((ParagraphTag) tags);
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof ArticleTag;
		}
	}

	public static class ParagraphTag implements Tags {
		public String tagName;
		public String textIndent;
		public String marginLeft;
		public String text;

		@Override
		public void addTag(Tags tags) {
			//TODO: addTag Exception 하위 없음
		}

		@Override
		public boolean equalsClass(Tags tags) {
			return tags instanceof ParagraphTag;
		}
	}

	public static interface Tags {
		void addTag(Tags tags);
		boolean equalsClass(Tags tags);
	}
}