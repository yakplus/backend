package com.likelion.backendplus4.yakplus.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

/**
 * API 응답 포맷 클래스 정상 및 에러 응답을 통합된 형식으로 제공한다.
 *
 * @since 2025-04-16
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private static final String SUCCESS_MESSAGE = "요청 성공";

    private String errorCode;
    private String message;
    private T data;

    /**
     * 정상 응답 생성 (데이터가 있거나 없을 때 사용)
     *
     * @param <T>  데이터 타입
     * @param data (선택) 응답 데이터
     * @return 200 OK 응답
     * @since 2025-04-17
     * @author 박찬병
     * @modify 2025-04-17 박찬병
     */
    @SafeVarargs
    public static <T> ResponseEntity<ApiResponse<T>> success(T... data) {
        // 1) 가변 길이 인자에서 첫 번째 요소를 꺼냄
        T responseData = (data != null && data.length > 0) ? data[0] : null;

        // 2) ApiResponse 객체를 빌더로 생성
        ApiResponse<T> body = ApiResponse.<T>builder()
            .errorCode(null)                // 에러코드 없음
            .message(SUCCESS_MESSAGE)       // "요청 성공"
            .data(responseData)             // data가 있으면 첫 번째, 없으면 null
            .build();

        // 3) HTTP 200 OK + body를 함께 반환
        return ResponseEntity.ok(body);
    }



    // // 1) 성공 (데이터 없음)
    // public static ResponseEntity<ApiResponse<Void>> success() {
    //     ApiResponse<Void> body = ApiResponse.<Void>builder()
    //         .errorCode(null)
    //         .message(SUCCESS_MESSAGE)
    //         .data(null)
    //         .build();
    //     return ResponseEntity.ok(body);
    // }
    //
    // // 2) 성공 (데이터 있음)
    // public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
    //     ApiResponse<T> body = ApiResponse.<T>builder()
    //         .errorCode(null)
    //         .message(SUCCESS_MESSAGE)
    //         .data(data)
    //         .build();
    //     return ResponseEntity.ok(body);
    // }

    /**
     * 에러 응답 생성
     *
     * @param <T>       데이터 타입
     * @param status    HTTP 상태 코드
     * @param errorCode 에러 코드
     * @param message   에러 메시지
     * @return 에러 응답 ResponseEntity
     * @since 2025-04-16
     * @author 정안식
     * @modify 2025-04-16 박찬병
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorCode, String message) {
        ApiResponse<T> body = ApiResponse.<T>builder()
            .errorCode(errorCode)
            .message(message)
            .data(null)
            .build();
        return ResponseEntity.status(status).body(body);
    }

}
