package com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.in.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in.DrugDataUseCase;
import com.likelion.backendplus4.yakplus.scraper.drug.detail.domain.model.GovDrug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ApiDataTestController {
	private final DrugDataUseCase drugDataUseCase;

	@GetMapping("/data/all")
	public List<GovDrug> getAllData(){
		log.info("getAllData");
		return drugDataUseCase.findAllRawDrug();
	}

}
