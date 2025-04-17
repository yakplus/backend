package com.likelion.backendplus4.yakplus.scraper.drug.img;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test2")
public class Test2Ctrl {
    private final ImageScraper imageScraper;

    @GetMapping
    public void test(){
        imageScraper.getApiData();
    }
}
