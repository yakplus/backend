package com.likelion.backendplus4.yakplus.search.presentation.controller;

import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;

public enum SearchType {
	SYMPTOM,
	NAME,
	INGREDIENT;

	public static SearchType from(String type) {
		try {
			return SearchType.valueOf(type.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new SearchException(SearchErrorCode.INVALID_SEARCH_TYPE);
		}
	}
}
