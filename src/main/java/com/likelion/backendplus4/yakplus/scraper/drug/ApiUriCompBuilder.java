package com.likelion.backendplus4.yakplus.scraper.drug;

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
 * @author 함예정
 */
@Component
public class ApiUriCompBuilder {
    private final String SERVICE_KEY;
    private final String HOST;
    private final String API_DETAIL_PATH;
    private final String API_IMG_PATH ;
    private final String RESPONSE_TYPE;

    public ApiUriCompBuilder(@Value("${gov.host}") String host,
                            @Value("${gov.serviceKey}") String serviceKey,
                            @Value("${gov.path.detail}") String pathDetail,
                            @Value("${gov.path.img}") String pathImg,
                            @Value("${gov.type}") String type) {
        this.HOST = host;
        this.SERVICE_KEY = serviceKey;
        this.API_DETAIL_PATH = pathDetail;
        this.API_IMG_PATH = pathImg;
        this.RESPONSE_TYPE = type;
    }

    /***
     * 입력 받은 path를 반영해 URI 객체를 생성, 반환
     *
     * @param path API 요청 경로
     * @return URI
     *
     * @since 2025-04-15
     * @author 함예정
     */
    private URI getUri(String path) {
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

    /***
     * 식품의약품안전처 의약품 제품 허가 상세 정보 URI 반환
     * @return URI 제품 허가 상세 정보
     * 
     * @since 2025-04-15
     * @author 함예정
     */
    public URI getUriForDetailApi() {
        return getUri(API_DETAIL_PATH);
    }

    /***
     * 식품의약품안전처 의약품 제품 허가 목록 URI 반환
     * @return URI 제품 허가 목록
     *
     * @since 2025-04-15
     * @author 함예정
     */
    public URI getUriForImgApi() {
        return getUri(API_IMG_PATH);
    }

    // TODO 추후 삭제
    public URI getUriForImgApiBySeq(String seq) {
        // 임시 URI
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(HOST)
            .port(443)
            .path(API_IMG_PATH)
            .queryParam("serviceKey", SERVICE_KEY)
            .queryParam("type", RESPONSE_TYPE)
            .queryParam("numOfRows", 1)
            .queryParam("prdlst_Stdr_code", seq)
            .build(true)
            .toUri();

    }

}
