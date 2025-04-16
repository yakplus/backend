package com.likelion.backendplus4.yakplus.adapter.persistence;

import com.likelion.backendplus4.yakplus.domain.DrugImageInfo;
import com.likelion.backendplus4.yakplus.port.DrugImageInfoPort;
import com.likelion.backendplus4.yakplus.repo.DrugImageInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * DrugImageInfoJpaAdapter는 DrugImageInfoPort 인터페이스를 구현하여
 * Spring Data JPA Repository를 통한 이미지 데이터 접근을 제공한다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Component
@RequiredArgsConstructor
public class DrugImageInfoJpaAdapter implements DrugImageInfoPort {

    private final DrugImageInfoRepository repository;

    @Override
    public DrugImageInfo save(DrugImageInfo imageInfo) {
        return repository.save(imageInfo);
    }

    @Override
    public Optional<DrugImageInfo> findById(String itemSeq) {
        return repository.findById(itemSeq);
    }
}
