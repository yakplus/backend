package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugImageGovScraper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DrugImageController {
    private final DrugImageGovScraper imageScraper;

    @GetMapping("/gov/api/parser/image/start")
    public void test(){
        imageScraper.getApiData();
    }
}
