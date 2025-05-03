package com.likelion.backendplus4.yakplus.switcher.application;

import com.likelion.backendplus4.yakplus.switcher.application.port.in.EmbeddingRoutingUseCase;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;
import org.springframework.stereotype.Service;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@Service
public class EmbeddingRouter implements EmbeddingRoutingUseCase {
    private final EmbeddingSwitchPort switchPort;

    public EmbeddingRouter(EmbeddingSwitchPort switchPort) {
        this.switchPort = switchPort;
    }

    @Override
    public void switchEmbedding(String adapterBeanName) {
        log("임베딩 스위치 요청 수신 - 어댑터명: " + adapterBeanName);
        switchPort.switchTo(adapterBeanName);
    }

    @Override
    public String getAdapterBeanName() {
        log("현재 선택된 어댑터 빈 이름 요청");
        return switchPort.getAdapterBeanName();
    }
}
