package com.likelion.backendplus4.yakplus.logtest;

import com.likelion.backendplus4.yakplus.common.util.LogLevel;
import static com.likelion.backendplus4.yakplus.common.util.LogUtil.log;

import com.likelion.backendplus4.yakplus.logtest.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @GetMapping("/process")
    public String process() {
        log(LogLevel.INFO, "Controller: Start processing");

        try {
            // 서비스 호출
            String result = myService.processData();
            log("Controller: Processing complete");
            return result;
        } catch (Exception e) {
            log(LogLevel.ERROR, "Controller: Error occurred while processing");
            return "Error occurred!";
        }
    }
}
