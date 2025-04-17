package com.likelion.backendplus4.yakplus.common.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Getter
public class LoggerWithTraceId {
    private final Logger logger;
    private final String traceId;

    // 생성자에서 Logger와 traceId를 초기화
    private LoggerWithTraceId(Logger logger, String traceId) {
        this.logger = logger;
        this.traceId = traceId;
    }

    // Logger와 traceId를 함께 가져오는 정적 팩토리 메서드
    public static LoggerWithTraceId create() {
        // traceId가 null일 경우 기본값 설정
        String traceId = makeTraceId();

        // getLogger 호출 시도, 예외 처리 추가
        Logger logger = makeLogger();

        return new LoggerWithTraceId(logger, traceId);  // Logger와 traceId를 담은 객체 반환
    }

    private static Logger makeLogger() {
        Logger logger = LoggerFactory.getLogger(getCallingClassName());
        if (logger == null) {
            throw new IllegalStateException("Logger creation failed for the class: " + getCallingClassName());
        }
        return logger;
    }

    private static String makeTraceId() {
        String traceId = MDC.get("traceId");
        if (traceId == null || traceId.isEmpty()) {
            // traceId가 null 또는 빈 값일 경우 예외를 던지거나 기본값을 설정
            traceId = "default-trace-id";  // 기본 traceId 설정
            throw new IllegalStateException("TraceId cannot be null or empty");
        }
        return traceId;
    }

    // 호출된 클래스 정보 자동으로 추출
    private static String getCallingClassName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // 스택 트레이스의 길이가 예상보다 짧을 수 있으므로 안전한 인덱스 접근 처리
        if (stackTraceElements.length < 4) {
            // 예상보다 스택이 짧을 경우, 호출된 클래스가 없다고 간주하고 기본 값 반환
            return "UnknownClass";
        }
        // 기본적으로 stackTraceElements[4]를 사용하여 호출된 클래스 추출
        return stackTraceElements[4].getClassName();
    }
}
