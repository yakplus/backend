package com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WarningType {
	WARNING("경고"),
	DO_NOT_ADMINISTER("다음 환자에는 투여하지 말 것."),
	CAUTION_ADMINISTER("다음 환자에는 신중히 투여할 것."),
	ADVERSE_REACTIONS("이상반응"),
	GENERAL_CAUTION("일반적 주의"),
	PREGNANCY("임부에 대한 투여"),
	PREGNANCY2("임부, 수유부, 가임여성, 신생아, 유아, 소아, 고령자에 대한 투여"),
	PEDIATRIC("소아에 대한 투여"),
	ELDERLY("고령자에 대한 투여"),
	OVERDOSE("과량투여시의 처치"),
	USAGE_NOTES("적용상의 주의"),
	STORE_NOTES("보관 및 취급상의 주의사항");

	private final String label;

	WarningType(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	@JsonCreator
	public static WarningType fromLabel(String title) {
		String cleaned = removeLeadingNumber(title);
		for (WarningType type : values()) {
			if (type.label.equals(cleaned)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown title: " + title);
	}

	private static String removeLeadingNumber(String title) {
		return title.replaceFirst("^\\d+\\.\\s*", ""); // 숫자 + 점 + 공백 제거
	}
}
