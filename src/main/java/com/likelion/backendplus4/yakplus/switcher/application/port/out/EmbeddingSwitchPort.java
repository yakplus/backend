package com.likelion.backendplus4.yakplus.switcher.application.port.out;

public interface EmbeddingSwitchPort {

    /**
     * 지정된 Bean 이름에 해당하는 어댑터로 전환합니다.
     *
     * @param adapterBeanName 전환할 어댑터 Bean 이름
     * @throws IllegalArgumentException 지원되지 않는 어댑터 이름인 경우
     * @author 정안식
     * @since 2025-05-02
     */
    void switchTo(String adapterBeanName);

    /**
     * 현재 활성화된 어댑터 Bean 이름을 반환합니다.
     *
     * @return 활성화된 어댑터 Bean 이름
     * @author 정안식
     * @since 2025-05-02
     */
    String getAdapterBeanName();
}
