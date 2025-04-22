package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugDataService;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DrugDataTestController {
	private final DrugDataService dragDataService;

	@GetMapping("/data/all")
	public List<GovDrug> getAllData(){
		log.info("getAllData");
		return dragDataService.findAllRawDrug();
	}

}
