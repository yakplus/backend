package com.likelion.backendplus4.yakplus.common.util.log;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * TraceId를 포함한 로거 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@Getter
public class LoggerWithTraceId {
    private final Logger logger;
    private final String traceId;

    /**
     * LoggerWithTraceId 생성자
     *
     * @param logger Logger 로거 객체
     * @param traceId String 추적 ID
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private LoggerWithTraceId(Logger logger, String traceId) {
        this.logger = logger;
        this.traceId = traceId;
    }

    /**
     * LoggerWithTraceId 인스턴스를 생성하는 팩토리 메서드
     *
     * @return LoggerWithTraceId 생성된 로거 인스턴스
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public static LoggerWithTraceId create() {
        String traceId = makeTraceId();
        Logger logger = makeLogger();
        return new LoggerWithTraceId(logger, traceId);
    }

    /**
     * 로거를 생성하는 메서드
     *
     * @return Logger 생성된 로거
     * @throws IllegalStateException 로거 생성 실패 시
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
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

    /**
     * TraceId를 생성하는 메서드
     *
     * @return String 생성된 TraceId
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private static String makeTraceId() {
        String traceId = MDC.get("traceId");
        if (traceId == null || traceId.trim().isEmpty()) {
            return "no-trace";
        }
        return traceId;
    }

    /**
     * 호출한 클래스의 이름을 가져오는 메서드
     *
     * @return String 호출한 클래스명
     * @throws IllegalStateException 스택 트레이스 관련 오류 발생 시
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
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
