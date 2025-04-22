package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogMessage;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 데이터 처리를 위한 서비스 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@Service
public class MyService {

    private static final long PROCESSING_DELAY = 1000L;

    /**
     * 데이터를 처리하는 메서드
     *
     * @return String 처리된 데이터 결과
     * @throws RuntimeException 처리 중 오류 발생 시
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    public String processData() {
        log(LogLevel.INFO, LogMessage.SERVICE_DATA_PROCESSING_START.getMessage());

        try {
            simulateProcessing();
            log(LogLevel.INFO, LogMessage.SERVICE_DATA_PROCESSING_SUCCESS.getMessage());
            return LogMessage.PROCESSED_DATA_RESULT.getMessage();
        } catch (InterruptedException e) {
            log(LogLevel.ERROR, LogMessage.SERVICE_DATA_PROCESSING_ERROR.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new RuntimeException(LogMessage.SERVICE_DATA_PROCESSING_ERROR.getMessage(), e);
        }
    }

    /**
     * 처리 과정을 시뮬레이션하는 private 메서드
     *
     * @throws InterruptedException 인터럽트 발생 시
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    private void simulateProcessing() throws InterruptedException {
        Thread.sleep(PROCESSING_DELAY);
    }
}
