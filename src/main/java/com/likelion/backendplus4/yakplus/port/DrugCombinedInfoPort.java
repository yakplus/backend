package com.likelion.backendplus4.yakplus.port;

import com.likelion.backendplus4.yakplus.domain.DrugCombinedInfo;
import java.util.Optional;

/**
 * DrugCombinedInfoPort는 결합된 의약품 정보와 관련된 동작(저장, 조회 등)을 추상화한 포트 인터페이스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
public interface DrugCombinedInfoPort {
    DrugCombinedInfo save(DrugCombinedInfo combinedInfo);
    Optional<DrugCombinedInfo> findById(String itemSeq);
}
