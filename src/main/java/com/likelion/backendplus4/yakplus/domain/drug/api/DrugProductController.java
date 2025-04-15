package com.likelion.backendplus4.yakplus.domain.drug.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.domain.drug.application.DrugProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drug")
@RequiredArgsConstructor
public class DrugProductController {

	private final DrugProductService drugProductService;

	@GetMapping("/import")
	public ResponseEntity<String> importDrugProducts() {
		drugProductService.fetchAndSaveDrugProducts();
		return ResponseEntity.ok("의약품 데이터를 성공적으로 가져와 DB에 저장했습니다.");
	}
}