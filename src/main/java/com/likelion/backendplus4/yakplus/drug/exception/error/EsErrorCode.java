package com.likelion.backendplus4.yakplus.drug.exception.error;

import org.springframework.http.HttpStatus;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EsErrorCode implements ErrorCode {
	ES_SUGGEST_SEARCH_FAIL(440001, HttpStatus.INTERNAL_SERVER_ERROR, "검색어 자동완성에 실패했습니다."),
	ES_SEARCH_FAIL(440002, HttpStatus.INTERNAL_SERVER_ERROR, "증상 검색에 실패했습니다.");

	private final int codeNumber;
	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return httpStatus;
	}

	@Override
	public int codeNumber() {
		return codeNumber;
	}

	@Override
	public String message() {
		return message;
	}
}
