package com.likelion.backendplus4.yakplus.search.application.port.out.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SearchByKeywordParams {
    private final String query;
    private final int from;
    private final int size;
}
