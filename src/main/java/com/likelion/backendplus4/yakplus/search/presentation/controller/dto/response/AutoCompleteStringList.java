package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response;

import java.util.List;

/**
 * 자동완성 검색 결과 DTO
 * @since 2025-04-28
 * @modified 2025-04-28
 */
public record AutoCompleteStringList(
	List<String> autoCompleteList
) {
}
