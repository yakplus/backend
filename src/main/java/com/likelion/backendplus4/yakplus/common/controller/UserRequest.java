package com.likelion.backendplus4.yakplus.common.controller;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
	@NotBlank
	String username,

	@NotBlank
	String nickname
) {
}
