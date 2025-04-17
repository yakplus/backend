package com.likelion.backendplus4.yakplus.common.util;

public class LogUtil {

    // 기본 log 메서드: 메시지만 전달하면 자동으로 INFO 레벨로 로그를 기록
    public static void log(String message) {
        log(LogLevel.INFO, message);  // 기본적으로 INFO 레벨로 처리
    }

    // 로그 레벨에 맞게 자동으로 로그 출력 (예외 없이)
    public static void log(LogLevel level, String message) {
        LoggerWithTraceId logAndTrace = LoggerWithTraceId.create();  // Logger와 traceId를 자동으로 처리
        level.log(logAndTrace.getLogger(), logAndTrace.getTraceId(), message);  // enum에서 정의한 log() 메서드 호출
    }

    // 로그 레벨에 맞게 자동으로 로그 출력 (예외 포함, ERROR일 때만 예외 처리)
    public static void log(LogLevel level, String message, Throwable t) {
        LoggerWithTraceId logAndTrace = LoggerWithTraceId.create();  // Logger와 traceId를 자동으로 처리
        level.log(logAndTrace.getLogger(), logAndTrace.getTraceId(), message, t);  // ERROR 레벨에서 예외가 처리되도록 호출
    }
}
