package com.likelion.backendplus4.yakplus.search.application.port.out;

public interface EmbeddingPort {

    /**
     * 현재 선택된 어댑터로부터 임베딩 벡터를 반환합니다.
     *
     * @param text 임베딩을 생성할 입력 문자열
     * @return 입력 문자열에 대한 임베딩 벡터 배열
     * @throws IllegalStateException 어댑터가 선택되지 않은 경우
     * @author 정안식
     * @since 2025-05-02
     */
    float[] getEmbedding(String text);
}