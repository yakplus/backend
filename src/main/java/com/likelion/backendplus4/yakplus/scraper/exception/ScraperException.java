package com.likelion.backendplus4.yakplus.scraper.exception;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

public class ScraperException extends CustomException {
    private static final long serialVersionUID = 1L;
    private final ErrorCode errorCode;

    public ScraperException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
