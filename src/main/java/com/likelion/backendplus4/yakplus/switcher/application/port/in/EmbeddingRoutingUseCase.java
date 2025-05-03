package com.likelion.backendplus4.yakplus.switcher.application.port.in;

public interface EmbeddingRoutingUseCase {
    void switchEmbedding(String adapterBeanName);
    String getAdapterBeanName();
}