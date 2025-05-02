package com.likelion.backendplus4.yakplus.switcher.application.port.out;

public interface EmbeddingSwitchPort {
    void switchTo(String adapterBeanName);
    String getAdapterBeanName();
}
