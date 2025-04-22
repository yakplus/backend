package com.likelion.backendplus4.yakplus.drug.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Material {
	@JsonProperty("성분명")
	private String name;

	@JsonProperty("분량")
	private String amount;

	@JsonProperty("단위")
	private String unit;

	@JsonProperty("총량")
	private String totalAmount;

	@JsonProperty("규격")
	private String standard;

	@JsonProperty("비고")
	private String note;

	@JsonProperty("성분정보")
	private String info;
}
