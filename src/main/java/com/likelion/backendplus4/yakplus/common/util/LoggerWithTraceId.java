package com.likelion.backendplus4.yakplus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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
        String traceId = MDC.get("traceId");  // MDC에서 traceId 가져오기
        Logger logger = LoggerFactory.getLogger(getCallingClassName());  // 자동으로 Logger 객체 생성
        return new LoggerWithTraceId(logger, traceId);  // Logger와 traceId를 담은 객체 반환
    }

    // 호출된 클래스 정보 자동으로 추출
    private static String getCallingClassName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // StackTraceElement[2]는 실제로 로그를 호출한 클래스 정보
        if (stackTraceElements.length > 2) {
            return stackTraceElements[2].getClassName();
        }
        return "UnknownClass";  // 예외 처리
    }

    public Logger getLogger() {
        return logger;
    }

    public String getTraceId() {
        return traceId;
    }
}
