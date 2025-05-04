package com.likelion.backendplus4.yakplus.switcher.infrastructure.route.adapter;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * EmbeddingPort 및 EmbeddingSwitchPort를 구현하여
 * 어댑터 간 전환과 현재 어댑터 조회 기능을 제공하는 컴포넌트
 *
 * @since 2025-05-02
 */
@Component("embeddingRouterAdapter")
@Primary
public class EmbeddingRouterAdapter implements EmbeddingPort, EmbeddingSwitchPort {
    @Value("${embed.switcher.default-adapter}")
    private String DEFAULT_ADAPTER;
    private final Map<String, EmbeddingPort> adapters;
    private volatile EmbeddingPort embeddingPort;
    private volatile String adapterBeanName;

    /**
     * 모든 EmbeddingPort 구현체를 주입받아 어댑터 라우팅 맵을 초기화합니다.
     *
     * @param allAdapters 어댑터 빈 이름을 키로 하고 EmbeddingPort 구현체를 값으로 갖는 맵
     * @author 정안식
     * @since 2025-05-02
     */
    public EmbeddingRouterAdapter(Map<String, EmbeddingPort> allAdapters) {
        this.adapters = allAdapters;
        log("구현체 목록: " + adapters.keySet());
    }

    /**
     * 컴포넌트 초기화 후 기본 어댑터로 전환합니다.
     *
     * @author 정안식
     * @since 2025-05-02
     */
    @PostConstruct
    public void init() {
        log("EmbeddingRouterAdapter 초기화 - 어댑터명: " + DEFAULT_ADAPTER);
        switchTo(DEFAULT_ADAPTER);
    }

    /**
     * 현재 선택된 어댑터로부터 임베딩 벡터를 반환합니다.
     *
     * @param text 임베딩을 생성할 입력 문자열
     * @return 입력 문자열에 대한 임베딩 벡터 배열
     * @throws IllegalStateException 어댑터가 선택되지 않은 경우
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public float[] getEmbedding(String text) {
        if (embeddingPort == null) {
            log(LogLevel.ERROR, "임베딩 어댑터가 선택되지 않았습니다.");
            throw new IllegalStateException("No adapter selected");
        }
        return embeddingPort.getEmbedding(text);
    }

    /**
     * 지정된 Bean 이름에 해당하는 어댑터로 전환합니다.
     *
     * @param adapterBeanName 전환할 어댑터 Bean 이름
     * @throws IllegalArgumentException 지원되지 않는 어댑터 이름인 경우
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public void switchTo(String adapterBeanName) {
        log("어댑터 스위치 시도 - 어댑터명: " + adapterBeanName);
        EmbeddingPort target = adapters.get(adapterBeanName);
        if (target == null) {
            log(LogLevel.ERROR, "어댑터 빈을 찾을 수 없습니다: " + adapterBeanName);
            throw new IllegalArgumentException("Unknown adapter: " + adapterBeanName);
        }
        this.embeddingPort = target;
        this.adapterBeanName = adapterBeanName;
        log("어댑터 스위치 완료 - 현재 어댑터: " + adapterBeanName);
    }

    /**
     * 현재 활성화된 어댑터 Bean 이름을 반환합니다.
     *
     * @return 활성화된 어댑터 Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    @Override
    public String getAdapterBeanName() {
        log("어댑터 빈 이름 요청 - 현재 선택된 어댑터: " + adapterBeanName);
        return adapterBeanName;
    }
}
