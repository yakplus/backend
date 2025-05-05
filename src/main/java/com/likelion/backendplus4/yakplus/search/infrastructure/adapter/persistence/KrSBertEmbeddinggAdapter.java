package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.dto.EmbeddingRequestText;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.UriCompBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KrSBertEmbeddinggAdapter implements EmbeddingPort {
    private final UriCompBuilder apiUriCompBuilder;
    private final RestTemplate restTemplate;

    @Override
    public float[] getEmbedding(String text) {
        URI embeddingURI = getEmbeddingURI();
        return getEmbeddingVector(embeddingURI, text);
    }

    private URI getEmbeddingURI() {
        return apiUriCompBuilder.getUriForKrSbertEmbeding();
    }

    private float[] getEmbeddingVector(URI embedUri, String text) {
        EmbeddingRequestText embeddingRequestText = new EmbeddingRequestText();
        embeddingRequestText.setText(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmbeddingRequestText> request = new HttpEntity<>(embeddingRequestText, headers);
        return restTemplate.postForObject(embedUri, request, float[].class);
    }
}
