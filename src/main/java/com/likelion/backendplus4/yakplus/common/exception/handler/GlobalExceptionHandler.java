package com.likelion.backendplus4.yakplus.common.exception.handler;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import com.likelion.backendplus4.yakplus.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 클래스 컨트롤러에서 발생한 예외를 공통적으로 처리한다.
 *
 * @since 2025-02-18
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리 ErrorCode 인터페이스 기반으로 확장 가능한 방식으로 처리한다.
     *
     * @param ex CustomException 객체
     * @return 에러 응답
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        log.error("CustomException occurred: {}", ex.getMessage(), ex);

        ErrorCode errorCode = ex.getErrorCode();

        return ApiResponse.error(errorCode.httpStatus(), String.valueOf(errorCode.codeNumber()), errorCode.message());
    }

    /**
     * IllegalArgumentException 처리 잘못된 파라미터에 대한 예외 응답 처리
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: {}", ex.getMessage(), ex);

        return ApiResponse.error(HttpStatus.BAD_REQUEST, "300000", ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException 처리 유효성 검사 실패에 대한 응답 처리
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage(), ex);

        return ApiResponse.error(HttpStatus.BAD_REQUEST, "300001", "유효성 검사 실패");
    }

    /**
     * 기타 모든 예외 처리 정의되지 않은 예외는 내부 서버 오류로 응답
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "500000", "내부 서버 오류");
    }
}

