package com.likelion.backendplus4.yakplus.els.core;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingDrugService {
    private final OpenAiApi openAiApi;

    public EmbeddingDrugService(OpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
    }

    public float[] getEmbedding(String text) {
        var model = new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model("text-embedding-3-small")
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );
        EmbeddingResponse resp = model.embedForResponse(List.of(text));
        return resp.getResults().getFirst().getOutput();
    }
}