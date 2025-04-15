package com.likelion.backendplus4.yakplus.scraper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestCtrl {
    private final ApprovedDrugList approvedDrugList;

    @GetMapping
    public void test(){
        approvedDrugList.getAPIData();
    }
}
