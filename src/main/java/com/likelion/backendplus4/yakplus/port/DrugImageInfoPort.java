package com.likelion.backendplus4.yakplus.port;

import com.likelion.backendplus4.yakplus.domain.DrugImageInfo;
import java.util.Optional;

/**
 * DrugImageInfoPort는 의약품 이미지 정보와 관련된 동작(저장, 조회 등)을 추상화한 포트 인터페이스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
public interface DrugImageInfoPort {
    DrugImageInfo save(DrugImageInfo imageInfo);
    Optional<DrugImageInfo> findById(String itemSeq);
}
