package com.likelion.backendplus4.yakplus.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

/**
 * 로깅을 위한 인터셉터 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";

    /**
     * 요청 처리 전에 실행되는 메서드
     *
     * @param request HttpServletRequest 요청 객체
     * @param response HttpServletResponse 응답 객체
     * @param handler Object 핸들러 객체
     * @return boolean 처리 계속 여부
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        String traceId = generateTraceId();
        setTraceId(traceId);
        log("TraceId 생성 성공 - " + traceId);
        return true;
    }

    /**
     * 요청 처리가 완료된 후 실행되는 메서드
     *
     * @param request HttpServletRequest 요청 객체
     * @param response HttpServletResponse 응답 객체
     * @param handler Object 핸들러 객체
     * @param ex Exception 예외 객체
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        clearTraceId();
    }

    /**
     * TraceId를 생성하는 메서드
     *
     * @return String 생성된 TraceId
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    /**
     * TraceId를 설정하는 메서드
     *
     * @param traceId String 설정할 TraceId
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * TraceId를 제거하는 메서드
     *
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private void clearTraceId() {
        MDC.clear();
    }
}
