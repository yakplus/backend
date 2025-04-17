package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class XMLParsing {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();


	private static class JsonWrapperFactory {
		public static JsonWrapper createWrapper(String tagName) {
			switch (tagName){
				case "SECTION":
					return new SectionWrapper();
					break;
				case "ARTICLE":
					return new ArticleWrapper();
					break;
			}
		}
	}

	private static interface JsonWrapper {
		<T extends JsonWrapper> T parseElement(Element element);
	}

	private static class SectionWrapper implements JsonWrapper{
		public Element section;
		public ObjectNode sectionJson;
		public List<ArticleWrapper> articles = new ArrayList<>();

		@Override
		public SectionWrapper parseElement(Element element) {
			this.section = section;
			sectionJson = mapper.createObjectNode();
			toJsonNodeFromElement(sectionJson, section);
			return this;
		}
	}

	private static class ArticleWrapper implements JsonWrapper{
		public Element article;
		public ObjectNode articleJson;

		@Override
		public ArticleWrapper parseElement(Element element){
			this.article = element;
			articleJson = mapper.createObjectNode();
			toJsonNodeFromElement(articleJson, article);
			return this;
		}
	}

	public static String toJsonFromXml(String xml) {
		ObjectNode rootJson = mapper.createObjectNode();

		try {
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			Document doc = getDocumentFromXML(xml, builder);
			Element docElement = doc.getDocumentElement();
			toJsonNodeFromElement(rootJson, docElement);

			List<SectionWrapper> sectionWrappers = new ArrayList<>();
			parseTag(doc, sectionWrappers, "SECTION");

			for (SectionWrapper wrapper : sectionWrappers) {
				parseTag(doc, sectionWrappers, "ARTICLE");
			}


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

			return rootJson.toPrettyString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static void parseTag(Document doc, List targetList, String tagName) {
		NodeList nodeList = doc.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			JsonWrapper wrapperInstance = JsonWrapperFactory.createWrapper(tagName);
			targetList.add(wrapperInstance.parseElement(element));
		}
	}

	private static void name(){

	}
	private static boolean existNode(NodeList sectionNodes) {
		return sectionNodes.getLength() > 0;
	}

	private static void toJsonNodeFromElement(ObjectNode jsonNode, Element element) {
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attr = (Attr) attributes.item(i);
			jsonNode.put(attr.getName(), attr.getValue());
		}
	}

	private static Document getDocumentFromXML(String xml, DocumentBuilder builder) throws SAXException, IOException {
		return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
	}

	public static String parseMaterial(String raw) throws Exception {
		ObjectMapper result = new ObjectMapper();
		ArrayNode resultArray = result.createArrayNode();
		String[] blocks = splitBlock(raw);
		parsingblocksAndPutArrayItem(blocks, resultArray);
		return convertString(result, resultArray);
	}

	private static void parsingblocksAndPutArrayItem(String[] blocks, ArrayNode resultArray) {
		for (String block : blocks) {
			block = block.trim();
			if (block.isEmpty()) {
				continue;
			}
			String[] pairs = splitByPipe(block);
			ObjectNode item = makeItem(pairs);
			resultArray.add(item);
		}
	}

	private static String convertString(ObjectMapper objectMapper, ArrayNode arrayNode) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
		} catch (JsonProcessingException e) {
			//TODO String 변환실패
			throw new RuntimeException(e);
		}
	}
	
	private static ObjectNode makeItem(String[] pairs) {
		ObjectNode item = new ObjectMapper().createObjectNode();
		for (String pair : pairs) {
			String[] kv = pair.split(":", 2);
			String key = kv[0].trim();
			String value = "";
			if(kv.length == 2){
				value = kv[1].trim();
			}
			item.put(key, value);
		}
		return item;
	}

	private static String[] splitByPipe(String block) {
		return block.split("\\|");
	}

	private static String[] splitBlock(String raw) {
		return raw.split(";");
	}

}
