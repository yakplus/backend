package com.likelion.backendplus4.yakplus.scraper.drug.img.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.scraper.drug.img.service.DrungImageGovScraper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DragImageController {
    private final DrungImageGovScraper imageScraper;

    @GetMapping("/gov/api/parser/image/start")
    public void test(){
        imageScraper.getApiData();
    }
}
