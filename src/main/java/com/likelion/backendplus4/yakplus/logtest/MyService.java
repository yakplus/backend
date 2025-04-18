package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

/**
 * 데이터 처리를 위한 서비스 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@Service
public class MyService {

    private static final String DATA_PROCESSING_START = "Service: 데이터 처리를 시작합니다";
    private static final String DATA_PROCESSING_SUCCESS = "Service: 데이터 처리가 완료되었습니다";
    private static final String DATA_PROCESSING_ERROR = "Service: 데이터 처리 중 오류가 발생했습니다";
    private static final String PROCESSED_DATA_RESULT = "처리된 데이터";
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
        log(LogLevel.INFO, DATA_PROCESSING_START);

        try {
            simulateProcessing();
            log(LogLevel.INFO, DATA_PROCESSING_SUCCESS);
            return PROCESSED_DATA_RESULT;
        } catch (InterruptedException e) {
            log(LogLevel.ERROR, DATA_PROCESSING_ERROR, e);
            Thread.currentThread().interrupt();
            throw new RuntimeException(DATA_PROCESSING_ERROR, e);
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
