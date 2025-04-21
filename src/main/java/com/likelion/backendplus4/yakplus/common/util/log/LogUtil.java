package com.likelion.backendplus4.yakplus.common.util.log;

/**
 * 로깅 유틸리티 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
public class LogUtil {

    /**
     * INFO 레벨로 로그를 기록하는 메서드
     *
     * @param message String 로그 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public static void log(String message) {
        logWithLevel(LogLevel.INFO, message);
    }

    /**
     * 지정된 레벨로 로그를 기록하는 메서드
     *
     * @param level LogLevel 로그 레벨
     * @param message String 로그 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public static void log(LogLevel level, String message) {
        logWithLevel(level, message);
    }

    /**
     * 예외와 함께 지정된 레벨로 로그를 기록하는 메서드
     *
     * @param level LogLevel 로그 레벨
     * @param message String 로그 메시지
     * @param t Throwable 예외 객체
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public static void log(LogLevel level, String message, Throwable t) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message, t);
    }

    /**
     * 내부적으로 로그를 기록하는 private 메서드
     *
     * @param level LogLevel 로그 레벨
     * @param message String 로그 메시지
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private static void logWithLevel(LogLevel level, String message) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message);
    }

}
