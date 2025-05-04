package com.likelion.backendplus4.yakplus.switcher.application.port.in;

public interface EmbeddingRoutingUseCase {

    /**
     * 지정된 adapter Bean 이름으로 Embedding adapter를 전환합니다.
     *
     * @param adapterBeanName 전환할 adapter Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    void switchEmbedding(String adapterBeanName);

    /**
     * 현재 활성화된 Embedding adapter Bean 이름을 반환합니다.
     *
     * @return 현재 adapter Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    String getAdapterBeanName();
}