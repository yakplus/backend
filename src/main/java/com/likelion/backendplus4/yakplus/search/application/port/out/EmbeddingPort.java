package com.likelion.backendplus4.yakplus.search.application.port.out;

public interface EmbeddingPort {
    float[] getEmbedding(String text);
}