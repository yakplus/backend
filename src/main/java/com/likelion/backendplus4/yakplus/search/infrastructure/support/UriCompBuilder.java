package com.likelion.backendplus4.yakplus.search.infrastructure.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/***
 * API 요청 URI 객체 생성 빌더
 *
 * application.yml에서 주입되는 속성 값으로,
 * API HOST, PATH를 확인해 URI 객체를 만듭니다.
 *
 * @since 2025-04-15
 */
@Component
public class UriCompBuilder {
    private final String API_KM_BERT;
    private final String API_KR_SBERT;

    public UriCompBuilder(
            @Value("${embed.kmbert}") String API_KM_BERT,
            @Value("${embed.krsbert}") String API_KR_SBERT) {
        this.API_KM_BERT = API_KM_BERT;
        this.API_KR_SBERT = API_KR_SBERT;
    }


    /**
     * KmBERT 임베딩 API 요청용 URI를 반환합니다.
     *
     * @return KmBERT API URI
     * @author 함예정
     * @since 2025-04-21
     */
    public URI getUriForKmbertEmbeding() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(API_KM_BERT)
                .port(443)
                .path("/predict")
                .build(true)
                .toUri();
    }

    /**
     * KrSBERT 임베딩 API 요청용 URI를 반환합니다.
     *
     * @return KrSBERT API URI
     * @author 함예정
     * @since 2025-04-21
     */
    public URI getUriForKrSbertEmbeding() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(API_KR_SBERT)
                .port(443)
                .path("/predict")
                .build(true)
                .toUri();
    }

}

