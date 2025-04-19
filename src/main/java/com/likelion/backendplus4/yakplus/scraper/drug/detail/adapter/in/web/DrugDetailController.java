package com.likelion.backendplus4.yakplus.scraper.drug.detail.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in.DrugApprovalDetailScraperUseCase;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DrugDetailController {
	private final DrugApprovalDetailScraperUseCase scraperUseCase;

	@GetMapping("/gov/api/parser/detail/start")
	public ResponseEntity saveAPIData(){
		scraperUseCase.requestUpdateRawData();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/gov/api/parser/detail/startAll")
	public ResponseEntity saveAPIDataAll(){
		scraperUseCase.requestUpdateAllRawData();
		return ResponseEntity.ok().build();
	}
}
