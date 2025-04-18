package com.likelion.backendplus4.yakplus.common.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Getter
public class LoggerWithTraceId {
    private final Logger logger;
    private final String traceId;

    private LoggerWithTraceId(Logger logger, String traceId) {
        this.logger = logger;
        this.traceId = traceId;
    }

    public static LoggerWithTraceId create() {
        String traceId = makeTraceId();
        Logger logger = makeLogger();
        return new LoggerWithTraceId(logger, traceId);
    }

    private static Logger makeLogger() {
        String callingClassName = getCallingClassName();
        if (callingClassName.trim().isEmpty()) {
            throw new IllegalStateException("호출 클래스명을 찾을 수 없습니다.");
        }

        Logger logger = LoggerFactory.getLogger(callingClassName);
        if (logger == null) {
            throw new IllegalStateException(String.format("클래스 '%s'에 대한 Logger 생성 실패", callingClassName));
        }
        return logger;
    }

    private static String makeTraceId() {
        String traceId = MDC.get("traceId");
        validateTraceId(traceId);
        return traceId;
    }

    private static void validateTraceId(String traceId) {
        if (traceId == null) {
            throw new IllegalStateException("TraceId가 null입니다. MDC에 traceId가 설정되어 있는지 확인하세요.");
        }
        if (traceId.trim().isEmpty()) {
            throw new IllegalStateException("TraceId가 빈 문자열입니다. 유효한 traceId를 설정해주세요.");
        }
    }

    private static String getCallingClassName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length == 0) {
            throw new IllegalStateException("스택 트레이스가 비어 있습니다.");
        }
        if (stackTraceElements.length < 5) {
            throw new IllegalStateException("스택 트레이스가 예상보다 짧습니다.");
        }

        String className = stackTraceElements[6].getClassName();
        if (className.trim().isEmpty()) {
            return "UnknownClass";
        }
        return className;
    }
}
