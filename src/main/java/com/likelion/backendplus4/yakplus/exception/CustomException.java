package com.likelion.backendplus4.yakplus.exception;


import com.likelion.backendplus4.yakplus.exception.error.ErrorCode;

/**
 * 사용자 정의 예외의 추상 클래스 애플리케이션 전역에서 사용하는 공통 예외 상위 타입이다.
 *
 * @since 2025-02-18
 */
public abstract class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 생성자 - ErrorCode의 메시지를 기반으로 예외 메시지를 설정한다.
     *
     * @param errorCode ErrorCode 객체
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.message());
    }

    /**
     * ErrorCode 반환
     *
     * @return ErrorCode 객체
     * @modified 2025-03-29
     * @author 정안식
     * @since 2025-02-18
     */
    public abstract ErrorCode getErrorCode();
}

