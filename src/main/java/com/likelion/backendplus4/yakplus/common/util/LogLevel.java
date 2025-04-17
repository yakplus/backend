package com.likelion.backendplus4.yakplus.common.util;

import org.slf4j.Logger;

public enum LogLevel {
    INFO {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logger.info(formatMessage(traceId, message));  // INFO 레벨 로그 출력
        }
    },
    DEBUG {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logger.debug(formatMessage(traceId, message));  // DEBUG 레벨 로그 출력
        }
    },
    ERROR {
        @Override
        public void log(Logger logger, String traceId, String message) {
            logger.error(formatMessage(traceId, message));  // ERROR 레벨 로그 출력
        }

        @Override
        public void log(Logger logger, String traceId, String message, Throwable t) {
            // ERROR 레벨에서만 예외 처리, 스택 트레이스를 로깅
            logger.error(formatMessage(traceId, message), t);  // ERROR 레벨에서 예외와 메시지를 함께 출력
        }
    };

    private static String formatMessage(String traceId, String message) {
        return String.format("TraceId: %s - %s", traceId, message);
    }

    public abstract void log(Logger logger, String traceId, String message);

    // INFO와 DEBUG는 예외를 처리하지 않음
    public void log(Logger logger, String traceId, String message, Throwable t) {
        // 예외를 처리하지 않으면 UnsupportedOperationException을 던져 예외를 처리하지 않음을 알림
        throw new UnsupportedOperationException("This level does not support exception logging.");
    }
}
