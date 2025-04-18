package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

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

    private static final String DATA_PROCESSING_START = "데이터 처리를 시작합니다";
    private static final String DATA_PROCESSING_SUCCESS = "데이터 처리가 성공적으로 완료되었습니다";
    private static final String DATA_PROCESSING_ERROR = "데이터 처리 중 오류가 발생했습니다";

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
        log(LogLevel.INFO, DATA_PROCESSING_START);

        try {
            String result = myService.processData();
            log(LogLevel.INFO, DATA_PROCESSING_SUCCESS);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log(LogLevel.ERROR, DATA_PROCESSING_ERROR, e);
            return ResponseEntity.internalServerError()
                    .body(DATA_PROCESSING_ERROR);
        }
    }
}
