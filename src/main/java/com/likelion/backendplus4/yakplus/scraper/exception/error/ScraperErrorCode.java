package com.likelion.backendplus4.yakplus.scraper.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ScraperErrorCode implements ErrorCode {

    DB_ERROR_PERMIT_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300001, "허가 정보를 조회하는데 실패했습니다."),
    DB_ERROR_IMAGE_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300002, "이미지 정보를 조회하는데 실패했습니다."),
    DB_ERROR_COMBINED_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300003, "결합된 정보를 조회하는데 실패했습니다."),
    API_CONNECT_FAIL(HttpStatus.BAD_GATEWAY, 400001, "외부 API 연결에 실패했습니다."),
    PARSING_ERROR(HttpStatus.BAD_REQUEST, 400001, "데이터 파싱에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public int codeNumber() {
        return 0;
    }

    @Override
    public String message() {
        return "";
    }
}
