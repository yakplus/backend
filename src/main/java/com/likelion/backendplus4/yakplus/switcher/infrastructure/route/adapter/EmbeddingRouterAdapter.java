package com.likelion.backendplus4.yakplus.switcher.infrastructure.route.adapter;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@Component("embeddingRouterAdapter")
@Primary
public class EmbeddingRouterAdapter implements EmbeddingPort, EmbeddingSwitchPort {
    private static final String DEFAULT_ADAPTER = "openAIEmbeddingAdapter";
    private final Map<String, EmbeddingPort> adapters;
    private volatile EmbeddingPort embeddingPort;
    private volatile String adapterBeanName;

    public EmbeddingRouterAdapter(Map<String, EmbeddingPort> allAdapters) {
        this.adapters = allAdapters;
        log("구현체 목록: " + adapters.keySet());
    }

    @PostConstruct
    public void init() {
        log("EmbeddingRouterAdapter 초기화 - 어댑터명: " + DEFAULT_ADAPTER);
        switchTo(DEFAULT_ADAPTER);
    }

    @Override
    public float[] getEmbedding(String text) {
        if (embeddingPort == null) {
            log(LogLevel.ERROR, "임베딩 어댑터가 선택되지 않았습니다.");
            throw new IllegalStateException("No adapter selected");
        }
        return embeddingPort.getEmbedding(text);
    }

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

    @Override
    public String getAdapterBeanName() {
        log("어댑터 빈 이름 요청 - 현재 선택된 어댑터: " + adapterBeanName);
        return adapterBeanName;
    }
}
