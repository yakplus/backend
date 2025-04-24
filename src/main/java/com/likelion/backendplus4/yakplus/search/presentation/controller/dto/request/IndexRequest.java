package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request;

/**
 * 인덱싱 요청 정보 DTO
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
public record IndexRequest(
        Long lastSeq,
        int limit) {
}