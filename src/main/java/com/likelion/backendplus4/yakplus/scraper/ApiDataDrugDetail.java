package com.likelion.backendplus4.yakplus.scraper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataDrugDetail {
	@JsonProperty("ITEM_SEQ")
	@Id
	private Long seq;

	@JsonProperty("ITEM_NAME")
	private String name;

	@JsonProperty("ENTP_NAME")
	private String company;

	@JsonProperty("ITEM_PERMIT_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private LocalDate permitDate;

	private boolean requiredPrescription;

	@JsonProperty("MATERIAL_NAME")
	@Column(columnDefinition = "TEXT")
	private String ingredient;

	@JsonProperty("STORAGE_METHOD")
	private String storageMethod;

	@JsonProperty("VALID_TERM")
	private String validTerm;

	@JsonProperty("EE_DOC_DATA")
	@Column(columnDefinition = "TEXT")
	private String efficacy;

	// @JsonProperty("UD_DOC_DATA")
	// private String usageRaw;
	@Setter
	@Column(columnDefinition = "TEXT")
	@Convert(converter = ListConverter.class)
	private List<String> drugUsage;

	// @JsonProperty("NB_DOC_DATA")
	// private String precautionsRaw;

	@Setter
	@Column(columnDefinition = "TEXT")
	@Convert(converter = PrecautionConverter.class)
	private Map<String,List<String>> precautions;

	@JsonCreator
	public ApiDataDrugDetail(@JsonProperty("ETC_OTC_CODE") String drugType) {
		this.requiredPrescription = "전문의약품".equals(drugType);
	}
	@Converter
	public static class ListConverter implements AttributeConverter<List<String>, String> {

		private static final ObjectMapper objectMapper = new ObjectMapper();

		@Override
		public String convertToDatabaseColumn(List<String> attribute) {
			try {
				return objectMapper.writeValueAsString(attribute);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException("리스트 → JSON 변환 실패", e);
			}
		}

		@Override
		public List<String> convertToEntityAttribute(String dbData) {
			try {
				return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
			} catch (IOException e) {
				throw new IllegalArgumentException("JSON → 리스트 복원 실패", e);
			}
		}
	}

	@Converter
	public static class PrecautionConverter implements AttributeConverter<Map<String, List<String>>, String> {

		private static final ObjectMapper objectMapper = new ObjectMapper();

		@Override
		public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
			try {
				return objectMapper.writeValueAsString(attribute);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException("Map 변환 실패", e);
			}
		}

		@Override
		public Map<String, List<String>> convertToEntityAttribute(String dbData) {
			try {
				return objectMapper.readValue(dbData, new TypeReference<LinkedHashMap<String, List<String>>>() {});
			} catch (IOException e) {
				throw new IllegalArgumentException("Map 복원 실패", e);
			}
		}
	}
}
