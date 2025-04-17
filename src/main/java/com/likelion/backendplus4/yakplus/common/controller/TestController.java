package com.likelion.backendplus4.yakplus.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<ApiResponse<Void>> test() {
		return ApiResponse.success();
	}

	@PostMapping("/test/bind")
	public ResponseEntity<String> createUser(@RequestBody @Valid UserRequest request) {
		// 이 메서드는 유효성 통과 시 정상 응답
		return ResponseEntity.ok("사용자 등록 성공: " + request.nickname());
	}
}
