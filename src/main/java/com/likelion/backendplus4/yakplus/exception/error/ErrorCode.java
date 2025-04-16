package com.likelion.backendplus4.yakplus.exception.error;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 인터페이스 각 에러 항목에 대한 HTTP 상태, 에러 번호, 메시지를 제공한다.
 *
 * @since 2025-02-18
 */
public interface ErrorCode {

    /**
     * HTTP 상태 반환
     *
     * @return HTTP 상태
     * @since 2025-02-18
     * @modified 2025-03-29
     * @author 정안식
     */
    HttpStatus httpStatus();

    /**
     * 에러 코드 번호 반환
     *
     * @return 에러 코드 번호
     * @since 2025-02-18
     * @modified 2025-03-29
     * @author 정안식
     */
    int codeNumber();

    /**
     * 에러 메시지 반환
     *
     * @return 에러 메시지
     * @since 2025-02-18
     * @modified 2025-03-29
     * @author 정안식
     */
    String message();
}
