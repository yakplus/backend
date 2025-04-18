package com.likelion.backendplus4.yakplus.common.exception;


import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

/**
 * 사용자 정의 예외의 추상 클래스 애플리케이션 전역에서 사용하는 공통 예외 상위 타입이다.
 *
 * @since 2025-04-16
 * @modify 2025-04-16
 */
public abstract class CustomException extends RuntimeException {

    /**
     * 생성자 - ErrorCode의 메시지를 기반으로 예외 메시지를 설정한다.
     *
     * @param errorCode ErrorCode 객체
     * @since 2025-04-16
     * @author 정안식
     * @modify 2025-04-16 정안식
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.message());
    }

    /**
     * ErrorCode 반환
     *
     * @return ErrorCode 객체
     * @since 2025-04-16
     * @author 정안식
     * @modify 2025-04-16 정안식
     */
    public abstract ErrorCode getErrorCode();
}

