package com.likelion.backendplus4.yakplus.scraper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ApiUriCompBuilder {
    private final String SERVICE_KEY;
    private final String HOST;
    private final String API_DETAIL_PATH;
    private final String API_IMG_PATH ;
    private final String RESPONSE_TYPE;

    public ApiUriCompBuilder(@Value("${api.host}") String host,
                            @Value("${api.serviceKey}") String serviceKey,
                            @Value("${api.path.detail}") String pathDetail,
                            @Value("${api.path.img}") String pathImg,
                            @Value("${api.type}") String type) {
        this.HOST = host;
        this.SERVICE_KEY = serviceKey;
        this.API_DETAIL_PATH = pathDetail;
        this.API_IMG_PATH = pathImg;
        this.RESPONSE_TYPE = type;
    }

    public URI getUri(String path) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(path)
            .queryParam("serviceKey", SERVICE_KEY)
            .queryParam("type", RESPONSE_TYPE)
            .queryParam("numOfRows", 100)
            .build(true)
            .toUri();
    }

    public URI getUriForDetailApi() {
        return getUri(API_DETAIL_PATH);
    }

    public URI getUriForImgApi() {
        return getUri(API_IMG_PATH);
    }

}
