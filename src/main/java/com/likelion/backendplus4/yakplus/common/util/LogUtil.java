package com.likelion.backendplus4.yakplus.common.util;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.logging.Level;

public class LogUtil {

    // 기본 log 메서드: 메시지만 전달하면 자동으로 INFO 레벨로 로그를 기록
    public static void log(String message) {
        // 기본적으로 INFO 레벨로 처리
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();
        LogLevel.INFO.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message);
    }

    // 로그 레벨에 맞게 자동으로 로그 출력 (예외 없이)
    public static void log(LogLevel level, String message) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();  // Logger와 traceId를 생성
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message);  // LogLevel에 맞는 로그 호출
    }

    // 로그 레벨에 맞게 자동으로 로그 출력 (예외 포함, ERROR일 때만 예외 처리)
    public static void log(LogLevel level, String message, Throwable t) {
        LoggerWithTraceId loggerWithTraceId = LoggerWithTraceId.create();  // Logger와 traceId를 자동으로 처리
        level.log(loggerWithTraceId.getLogger(), loggerWithTraceId.getTraceId(), message, t);  // ERROR 레벨에서 예외가 처리되도록 호출
    }

//    // 호출된 클래스 정보 자동으로 추출 - Throwable
//    private static String getCallingClassNameByThrowable() {
//        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
//        // 기본적으로 stackTraceElements[2]를 사용하여 호출된 클래스 추출
//        String callingClassName = stackTraceElements[3].getClassName();
//        System.out.println(callingClassName);
//        // 만약 호출된 클래스가 LogUtil이면 하나 더 깊이를 추가하여 [3]번째 항목을 가져옴
//        if ("jdk.internal.reflect.DirectMethodHandleAccessor".equals(callingClassName)) {
//            callingClassName = stackTraceElements[3].getClassName();  // 하나 더 깊이를 추가하여 [3]을 사용
//        }
//
//        return callingClassName;  // 해당 클래스 이름을 반환
//    }
}
