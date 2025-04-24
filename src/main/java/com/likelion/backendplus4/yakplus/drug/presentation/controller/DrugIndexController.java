package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.SymptomIndexService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index")
public class DrugIndexController {

	private final SymptomIndexService indexService;

	@PostMapping("/index")
	public ResponseEntity<String> triggerIndex() {
		try {
			indexService.indexAll();
			return ResponseEntity.ok("EEDoc indexing completed successfully.");
		} catch (Exception ex) {
			return ResponseEntity
				.status(500)
				.body("Indexing failed: " + ex.getMessage());
		}
	}


}
