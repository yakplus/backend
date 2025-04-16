package com.likelion.backendplus4.yakplus.scraper.drug.img;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.ToString;

@Entity
@ToString
public class ApiDataDrugImg {
	@Id
	@JsonProperty("ITEM_SEQ")
	private Long seq;

	@JsonProperty("BIG_PRDT_IMG_URL")
	private String imgUrl;
}
