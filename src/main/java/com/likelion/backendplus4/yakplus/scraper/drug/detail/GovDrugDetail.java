package com.likelion.backendplus4.yakplus.scraper.drug.detail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GovDrugDetail {

	@Id
	@JsonProperty("ITEM_SEQ")
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@JsonProperty("ITEM_NAME")
	@Column( name= "ITEM_NAME")
	private String drugName;

	@JsonProperty("ENTP_NAME")
	@Column( name= "ENTP_NAME")
	private String company;

	@JsonProperty("ITEM_PERMIT_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	@Column( name= "ITEM_PERMIT_DATE")
	private LocalDate permitDate;

	@Column(name = "ETC_OTC_CODE")
	private boolean isGeneral;

	@Column(name = "MATERIAL_NAME", columnDefinition = "JSON")
	private String materialInfo;

	@JsonProperty("STORAGE_METHOD")
	@Column(name = "STORAGE_METHOD")
	private String storeMethod;

	@JsonProperty("VALID_TERM")
	@Column(name = "VALID_TERM")
	private String validTerm;

	@Column(name = "EE_DOC_DATA", columnDefinition = "JSON")
	private String efficacy;

	@Column(name = "UD_DOC_DATA", columnDefinition = "JSON")
	private String usage;

	@Column(name = "NB_DOC_DATA", columnDefinition = "JSON")
	private String precaution;

	@JsonCreator
	public GovDrugDetail(@JsonProperty("ETC_OTC_CODE") String drugType) {
		this.isGeneral = !"전문의약품".equals(drugType);
	}

	public void changeMaterialInfo(String materialInfo){
		this.materialInfo = materialInfo;
	}

	public void changeUsage(String usage) {
		this.usage = usage;
	}

	public void changeEfficacy(String efficacy) {
		this.efficacy = efficacy;
	}

	public void changePrecaution(String precaution) {
		this.precaution = precaution;
	}

	public JsonNode getPrecaution() {
		return jsonConverter(precaution);
	}

	public JsonNode getUsage() {
		return jsonConverter(usage);
	}

	public JsonNode getEfficacy() {
		return jsonConverter(efficacy);
	}

	public JsonNode getMaterialInfo() {
		return jsonConverter(materialInfo);
	}

	private JsonNode jsonConverter(String json) {
		try {
			return new ObjectMapper().readValue(json, JsonNode.class);
		} catch (JsonProcessingException e) {
			//TODO 에러 로그 처리 필요합니다.
			throw new RuntimeException(e);
		}
	}
}
