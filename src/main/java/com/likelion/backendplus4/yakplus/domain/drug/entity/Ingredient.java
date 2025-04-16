package com.likelion.backendplus4.yakplus.domain.drug.entity;

import java.util.Map;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

	private String totalContent; // 총량
	private String name;         // 성분명
	private String amount;       // 분량
	private String unit;         // 단위
	private String standard;     // 규격
	private String info;         // 성분정보
	private String remark;       // 비고

	@Builder
	public Ingredient(String totalContent, String name, String amount, String unit, String standard, String info,
		String remark) {
		this.totalContent = totalContent;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.standard = standard;
		this.info = info;
		this.remark = remark;
	}

	public static Ingredient from(Map<String, String> map) {
		return Ingredient.builder()
			.totalContent(map.getOrDefault("총량", ""))
			.name(map.getOrDefault("성분명", ""))
			.amount(map.getOrDefault("분량", ""))
			.unit(map.getOrDefault("단위", ""))
			.standard(map.getOrDefault("규격", ""))
			.info(map.getOrDefault("성분정보", ""))
			.remark(map.getOrDefault("비고", ""))
			.build();
	}
}
