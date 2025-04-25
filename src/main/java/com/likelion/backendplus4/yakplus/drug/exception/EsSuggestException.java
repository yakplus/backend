package com.likelion.backendplus4.yakplus.drug.exception;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

public class EsSuggestException extends CustomException {
	private final ErrorCode errorCode;

	public EsSuggestException(ErrorCode errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
