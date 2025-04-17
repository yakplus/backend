package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.LogUtil;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

@Service
public class MyService {

    public String processData() {
        log("Service: Starting data processing");

        try {
            // 비즈니스 로직 예시
            Thread.sleep(1000); // 시간 지연 예시
            log("Service: Data processing finished successfully");
            return "Processed Data";
        } catch (InterruptedException e) {
            log(LogLevel.ERROR, "Service: Error during data processing", e); // 예외 객체 전달 (ERROR에서만 처리됨)
            throw new RuntimeException("Data processing failed", e);
        }
    }
}
