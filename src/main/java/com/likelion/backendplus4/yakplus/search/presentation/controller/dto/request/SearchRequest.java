package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * 검색 요청 정보 DTO
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
public record SearchRequest(
        String query,
        int page,
        int size) {
}
