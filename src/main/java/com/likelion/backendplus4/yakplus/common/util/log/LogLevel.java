package com.likelion.backendplus4.yakplus.common.util.log;

import org.slf4j.Logger;

/**
 * 로그 레벨을 정의하는 열거형 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
public enum LogLevel {
    /**
     * INFO 레벨 로그
     */
    INFO {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logMessage(logger::info, traceId, message);
        }
    },
    /**
     * DEBUG 레벨 로그
     */
    DEBUG {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logMessage(logger::debug, traceId, message);
        }
    },
    /**
     * ERROR 레벨 로그
     */
    ERROR {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logMessage(logger::error, traceId, message);
        }

        @Override
        public void log(Logger logger, String traceId, String message, Throwable t) {
            logger.error(formatMessage(traceId, message), t);
        }
    };

    /**
     * 로거 함수 인터페이스
     */
    @FunctionalInterface
    private interface LoggerFunction {
        void log(String message);
    }

    /**
     * 로그 메시지를 기록하는 메서드
     *
     * @param loggerFunction LoggerFunction 로거 함수
     * @param traceId String 추적 ID
     * @param message String 로그 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private static void logMessage(LoggerFunction loggerFunction, String traceId, String message) {
        loggerFunction.log(formatMessage(traceId, message));
    }

    /**
     * 로그 메시지를 포맷팅하는 메서드
     *
     * @param traceId String 추적 ID
     * @param message String 로그 메시지
     * @return String 포맷팅된 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private static String formatMessage(String traceId, String message) {
        return String.format("TraceId: %s - %s", traceId, message);
    }

    /**
     * 로그를 기록하는 추상 메서드
     *
     * @param logger Logger 로거 객체
     * @param traceId String 추적 ID
     * @param message String 로그 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public abstract void log(Logger logger, String traceId, String message);

    /**
     * 예외와 함께 로그를 기록하는 메서드
     *
     * @param logger Logger 로거 객체
     * @param traceId String 추적 ID
     * @param message String 로그 메시지
     * @param t Throwable 예외 객체
     * @throws UnsupportedOperationException 지원하지 않는 로그 레벨일 경우
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public void log(Logger logger, String traceId, String message, Throwable t) {
        throw new UnsupportedOperationException("이 로그 레벨은 예외 로깅을 지원하지 않습니다.");
    }
}
