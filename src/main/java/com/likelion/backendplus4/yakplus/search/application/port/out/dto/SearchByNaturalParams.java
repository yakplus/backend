package com.likelion.backendplus4.yakplus.search.application.port.out.dto;

import lombok.Getter;

@Getter
public class SearchByNaturalParams {
    private final String query;
    private final float[] vector;
    private final int from;
    private final int size;

    public SearchByNaturalParams(String query, float[] vector, int from, int size) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector는 null일 수 없습니다.");
        }
        this.query = query;
        this.vector = vector;
        this.from = from;
        this.size = size;
    }
}
