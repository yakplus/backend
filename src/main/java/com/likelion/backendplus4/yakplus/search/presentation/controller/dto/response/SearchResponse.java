package com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response;

import java.util.List;


import lombok.Builder;

/**
 * 검색 결과 정보 DTO
 *
 * @modified 2025-04-27
 * 25.04.27 - Drug 도메인 객체 변경에 따른 필드 수정
 * @since 2025-04-22
 */
@Builder
public record SearchResponse(
        Long drugId,
        String drugName,
        String company,
        List<String> efficacy,
        String imageUrl) {
}
