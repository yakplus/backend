package com.likelion.backendplus4.yakplus.infrastructure.adapter.persistence.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@ToString
@Table(name="API_DATA_DRUG_IMG")
public class ApiDataDrugImgEntity {
	@Id
	@JsonProperty("ITEM_SEQ")
	private Long seq;

	@JsonProperty("BIG_PRDT_IMG_URL")
	private String imgUrl;
}
