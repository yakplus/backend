package com.likelion.backendplus4.yakplus.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        String traceId = generateTraceId();
        setTraceId(traceId);
        log("TraceId 생성 성공 - " + traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        clearTraceId();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    private void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    private void clearTraceId() {
        MDC.clear();
    }
}
