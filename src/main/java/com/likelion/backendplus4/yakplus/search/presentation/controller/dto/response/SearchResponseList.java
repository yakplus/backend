package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response;

import java.util.List;

/**
 * 검색 결과 정보 리스트를 담는 DTO
 * @since 2025-04-28
 * @modified 2025-04-28
 */
public record SearchResponseList(
	List<SearchResponse> searchResponseList
) {
}
