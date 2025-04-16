package com.likelion.backendplus4.yakplus.adapter.persistence;

import com.likelion.backendplus4.yakplus.domain.DrugCombinedInfo;
import com.likelion.backendplus4.yakplus.port.DrugCombinedInfoPort;
import com.likelion.backendplus4.yakplus.repo.DrugCombinedInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * DrugCombinedInfoJpaAdapter는 DrugCombinedInfoPort 인터페이스를 구현하여
 * Spring Data JPA Repository를 통한 결합 정보 데이터 접근을 제공한다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Component
@RequiredArgsConstructor
public class DrugCombinedInfoJpaAdapter implements DrugCombinedInfoPort {

    private final DrugCombinedInfoRepository repository;

    @Override
    public DrugCombinedInfo save(DrugCombinedInfo combinedInfo) {
        return repository.save(combinedInfo);
    }

    @Override
    public Optional<DrugCombinedInfo> findById(String itemSeq) {
        return repository.findById(itemSeq);
    }
}
