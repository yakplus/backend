package com.likelion.backendplus4.yakplus.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.likelion.backendplus4.yakplus.application.port.in.DrugApprovalDetailScraperUseCase;

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

	@PostMapping("/gov/api/parser/detail/startAll")
	public ResponseEntity saveAPIDataAllByJdbc(){
		scraperUseCase.requestUpdateAllRawDataByJdbc();
		return ResponseEntity.ok().build();
	}
}
