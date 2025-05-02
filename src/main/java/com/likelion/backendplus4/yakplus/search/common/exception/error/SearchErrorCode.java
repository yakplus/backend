package com.likelion.backendplus4.yakplus.search.common.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
/**
 * 에러 코드 인터페이스 각 에러 항목에 대한 HTTP 상태, 에러 번호, 메시지를 제공한다.
 * A[BB][CCC]
 * A (1자리) : 에러 심각도 (1~5)
 * 1: 클라이언트 오류
 * 2: 인증 관련 오류
 * 3: 사용자 관련 오류
 * 4: 서버 오류
 * 5: 시스템 오류
 *
 * BB (2자리) : 도메인 코드
 * 10: 사용자 관련 (ex: USER_NOT_FOUND)
 * 20: 인증 관련 (ex: AUTHORIZATION_FAILED)
 * 30: DB 관련 오류 (ex: DB_CONNECTION_FAILED)
 * 40: API 관련 오류 (ex: API_TIMEOUT)
 * 50: 시스템 오류 (ex: INTERNAL_SERVER_ERROR)
 *
 * CCC (3자리) : 세부 오류 순번
 * 001: 첫 번째 오류
 * 002: 두 번째 오류
 * 003: 세 번째 오류, 등등
 *
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@RequiredArgsConstructor
public enum SearchErrorCode implements ErrorCode {
    INVALID_QUERY(HttpStatus.BAD_REQUEST, 140001, "검색어를 입력해주세요"),
    INVALID_PAGE(HttpStatus.BAD_REQUEST, 140002, "페이지 번호는 0 이상이어야 합니다."),
    INVALID_SIZE(HttpStatus.BAD_REQUEST, 140003, "페이지 크기는 1 이상이어야 합니다."),
    INVALID_SEARCH_TYPE(HttpStatus.BAD_REQUEST, 140004, "지원하지 않는 검색 타입입니다."),
    ES_SEARCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 430002, "Elasticsearch 검색 실패"),
    RDB_SEARCH_ERROR(HttpStatus.NO_CONTENT, 430003, "검색 결과가 없습니다"),
    EMBEDDING_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 440001, "임베딩 API 호출 실패"),
    ES_SUGGEST_SEARCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 440002, "검색어 자동완성에 실패했습니다."),
    ES_SEARCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 440002, "증상 검색에 실패했습니다.");

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
