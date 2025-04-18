package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

@Service
public class MyService {

    private static final String DATA_PROCESSING_START = "Service: 데이터 처리를 시작합니다";
    private static final String DATA_PROCESSING_SUCCESS = "Service: 데이터 처리가 완료되었습니다";
    private static final String DATA_PROCESSING_ERROR = "Service: 데이터 처리 중 오류가 발생했습니다";
    private static final String PROCESSED_DATA_RESULT = "처리된 데이터";
    private static final long PROCESSING_DELAY = 1000L;

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

    private void simulateProcessing() throws InterruptedException {
        Thread.sleep(PROCESSING_DELAY);
    }
}
