package com.likelion.backendplus4.yakplus.exception;

/**
 * DataIngestionException은 데이터 수집 과정에서 발생하는 예외를 처리하기 위한
 * 사용자 정의 예외 클래스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
public class DataIngestionException extends RuntimeException {

    /**
     * 메시지를 포함한 예외 생성자.
     *
     * @param message 예외 메시지
     */
    public DataIngestionException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 포함한 예외 생성자.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    public DataIngestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
