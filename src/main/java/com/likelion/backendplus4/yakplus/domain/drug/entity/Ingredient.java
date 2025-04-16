package com.likelion.backendplus4.yakplus.domain.drug.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Ingredient {

	private String totalContent; // 총량
	private String name;         // 성분명
	private String amount;       // 분량
	private String unit;         // 단위
	private String standard;     // 규격
	private String info;         // 성분정보
	private String remark;       // 비고

}
