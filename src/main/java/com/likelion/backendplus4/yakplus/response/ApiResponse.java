package com.likelion.backendplus4.yakplus.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * API 응답 포맷 클래스 정상 및 에러 응답을 통합된 형식으로 제공한다.
 *
 * @since 2025-02-18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String errorCode;
    private String message;
    private T data;

    /**
     * 생성자 - 응답 필드 초기화
     *
     * @param errorCode 에러 코드
     * @param message   응답 메시지
     * @param data      데이터 페이로드
     * @author 정안식
     * @modified 2025-03-29
     * @since 2025-02-18
     */
    private ApiResponse(String errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    /**
     * 정상 응답 생성
     *
     * @param <T>  데이터 타입
     * @param data 응답 데이터
     * @return 200 OK 응답
     * @author 정안식
     * @modified 2025-03-29
     * @since 2025-02-18
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(null, "요청 성공", data));
    }

    /**
     * 에러 응답 생성
     *
     * @param <T>       데이터 타입
     * @param status    HTTP 상태 코드
     * @param errorCode 에러 코드
     * @param message   에러 메시지
     * @return 에러 응답 ResponseEntity
     * @author 정안식
     * @modified 2025-03-29
     * @since 2025-02-18
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(errorCode, message, null));
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

