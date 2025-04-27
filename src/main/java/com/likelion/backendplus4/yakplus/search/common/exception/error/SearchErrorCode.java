package com.likelion.backendplus4.yakplus.search.common.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SearchErrorCode implements ErrorCode {
    INVALID_QUERY(HttpStatus.BAD_REQUEST, 140001, "검색어를 입력해주세요"),
    ES_SEARCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 430002, "Elasticsearch 검색 실패"),
    EMBEDDING_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 440001, "임베딩 API 호출 실패"),
    ES_SUGGEST_SEARCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 440002, "검색어 자동완성에 실패했습니다.");


    private final HttpStatus status;
    private final int code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return status;
    }

    @Override
    public int codeNumber() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
