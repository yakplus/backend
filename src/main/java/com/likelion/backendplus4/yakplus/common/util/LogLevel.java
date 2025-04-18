package com.likelion.backendplus4.yakplus.common.util;

import org.slf4j.Logger;

public enum LogLevel {
    INFO {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logMessage(logger::info, traceId, message);
        }
    },
    DEBUG {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logMessage(logger::debug, traceId, message);
        }
    },
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

    @FunctionalInterface
    private interface LoggerFunction {
        void log(String message);
    }

    private static void logMessage(LoggerFunction loggerFunction, String traceId, String message) {
        loggerFunction.log(formatMessage(traceId, message));
    }

    private static String formatMessage(String traceId, String message) {
        return String.format("TraceId: %s - %s", traceId, message);
    }

    public abstract void log(Logger logger, String traceId, String message);

    public void log(Logger logger, String traceId, String message, Throwable t) {
        throw new UnsupportedOperationException("이 로그 레벨은 예외 로깅을 지원하지 않습니다.");
    }
}
