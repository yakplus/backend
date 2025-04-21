package com.likelion.backendplus4.yakplus.common.util.log;

import lombok.Getter;

@Getter
public enum LogMessage {
    DATA_PROCESSING_START("데이터 처리를 시작합니다"),
    DATA_PROCESSING_SUCCESS("데이터 처리가 성공적으로 완료되었습니다"),
    DATA_PROCESSING_ERROR("데이터 처리 중 오류가 발생했습니다"),
    SERVICE_DATA_PROCESSING_START("Service: 데이터 처리를 시작합니다"),
    SERVICE_DATA_PROCESSING_SUCCESS("Service: 데이터 처리가 완료되었습니다"),
    SERVICE_DATA_PROCESSING_ERROR("Service: 데이터 처리 중 오류가 발생했습니다"),
    PROCESSED_DATA_RESULT("처리된 데이터"),
    TRACE_ID("traceId");

    private final String message;

    LogMessage(String message) {
        this.message = message;
    }

}