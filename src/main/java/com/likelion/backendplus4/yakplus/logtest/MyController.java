package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.common.util.log.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 데이터 처리를 위한 컨트롤러 클래스
 * 
 * @modified 2025-04-18
 * @since 2025-04-16
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    /**
     * 데이터 처리 요청을 처리하는 메서드
     *
     * @return ResponseEntity<String> 처리 결과
     * @author 정안식
     * @modified 2025-04-18
     * @since 2025-04-16
     */
    @GetMapping("/process")
    public ResponseEntity<String> process() {
        log(LogLevel.INFO, LogMessage.DATA_PROCESSING_START.getMessage());

        try {
            String result = myService.processData();
            log(LogLevel.INFO, LogMessage.DATA_PROCESSING_SUCCESS.getMessage());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log(LogLevel.ERROR, LogMessage.DATA_PROCESSING_ERROR.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(LogMessage.DATA_PROCESSING_ERROR.getMessage());
        }
    }
}
