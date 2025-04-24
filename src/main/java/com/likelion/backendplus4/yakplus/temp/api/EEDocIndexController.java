package com.likelion.backendplus4.yakplus.temp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.temp.application.EEDocIndexService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eedocs")
public class EEDocIndexController {

	private final EEDocIndexService indexService;

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
