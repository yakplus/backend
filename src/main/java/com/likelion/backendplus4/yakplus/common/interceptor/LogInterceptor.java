package com.likelion.backendplus4.yakplus.common.interceptor;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;
import java.util.UUID;

@Component
public class LogInterceptor implements HandlerInterceptor {

    // 요청 처리 전에 traceId 생성 및 설정
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        // traceId 생성
        String traceId = UUID.randomUUID().toString();
        // MDC에 traceId 추가
        MDC.put("traceId", traceId);
        // 로그 출력
        log(LogLevel.INFO ,"Generated traceId: " + traceId);
        return true;  // 계속해서 요청 처리 진행
    }

    // 요청 처리 후, traceId를 MDC에서 제거
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        // 요청이 끝난 후 traceId 제거
        MDC.clear();
    }
}
