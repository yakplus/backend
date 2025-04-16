package com.likelion.backendplus4.yakplus.adapter.persistence;

import com.likelion.backendplus4.yakplus.domain.DrugPermitInfo;
import com.likelion.backendplus4.yakplus.port.DrugPermitInfoPort;
import com.likelion.backendplus4.yakplus.repo.DrugPermitInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * DrugPermitInfoJpaAdapter는 DrugPermitInfoPort 인터페이스를 구현하여
 * Spring Data JPA Repository를 통한 데이터 접근을 제공한다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
@Component
@RequiredArgsConstructor
public class DrugPermitInfoJpaAdapter implements DrugPermitInfoPort {

    private final DrugPermitInfoRepository repository;

    @Override
    public DrugPermitInfo save(DrugPermitInfo info) {
        return repository.save(info);
    }

    @Override
    public List<String> findAllItemSeqs() {
        return repository.findAllItemSeqs();
    }

    @Override
    public Optional<DrugPermitInfo> findById(String itemSeq) {
        return repository.findById(itemSeq);
    }
}
