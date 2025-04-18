package com.likelion.backendplus4.yakplus.scraper.drug.detail.application.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.scraper.drug.detail.application.port.in.DrugApprovalDetailScraperUseCase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RawDataParseService {
	private final DrugApprovalDetailScraperUseCase drugApprovalDetailScraperUseCase;

	public void requestUpdateRawData() {
		drugApprovalDetailScraperUseCase.requestUpdateRawData();
	}


}
