package com.likelion.backendplus4.yakplus.common.util;

public class LogUtil {

    public static void log(String message) {
        logWithLevel(LogLevel.INFO, message);
    }

    public static void log(LogLevel level, String message) {
        logWithLevel(level, message);
    }

    public static void log(LogLevel level, String message, Throwable t) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message, t);
    }

    private static void logWithLevel(LogLevel level, String message) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message);
    }

}
