package com.likelion.backendplus4.yakplus.search.presentation.mapper;

import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.request.SearchRequest;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Presentation 계층의 Request 매핑 로직을 담당하는 클래스입니다.
 * HTTP 요청 파라미터를 SearchRequest 도메인 객체로 변환합니다.
 *
 * @since 2025-05-02
 */
public class SearchRequestMapper {
    /**
     * HTTP GET/POST 요청 파라미터를 SearchRequest DTO로 매핑합니다.
     *
     * @param query 검색어
     * @param page  페이지 번호 (0부터)
     * @param size  페이지 사이즈
     * @return SearchRequest 객체
     */
    public static SearchRequest toSearchRequest(String query, int page, int size) {
        return new SearchRequest(query, page, size);
    }
}
