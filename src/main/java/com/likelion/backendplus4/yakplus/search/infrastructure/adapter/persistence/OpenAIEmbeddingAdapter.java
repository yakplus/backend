package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAIEmbeddingAdapter implements EmbeddingPort {
    private final OpenAiApi openAiApi;
    private static final String EMBEDDING_MODEL = "text-embedding-3-small";
    
    @Override
    public float[] getEmbedding(String text) {
        OpenAiEmbeddingModel embeddingModel = createEmbeddingModel();
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));
        return response.getResults().getFirst().getOutput();
    }
    
    private OpenAiEmbeddingModel createEmbeddingModel() {
        return new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model(EMBEDDING_MODEL)
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );
    }
}