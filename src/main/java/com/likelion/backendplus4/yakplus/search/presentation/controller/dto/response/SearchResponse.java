package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response;

/**
 * 검색 결과 정보 DTO
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
public record SearchResponse(
        Long itemSeq,
        String itemName,
        String entpName,
        String eeText) {
}
