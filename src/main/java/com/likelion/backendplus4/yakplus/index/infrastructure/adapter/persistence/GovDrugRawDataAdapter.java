package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;
import com.likelion.backendplus4.yakplus.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.index.exception.error.IndexErrorCode;
import com.likelion.backendplus4.yakplus.index.infrastructure.entity.GovDrugRawDataEntity;
import com.likelion.backendplus4.yakplus.index.infrastructure.repository.RawDataJpaRepository;
import com.likelion.backendplus4.yakplus.index.domain.model.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 공공 API로부터 조회한 원시 약품 데이터를 JPA를 통해 가져와
 * 도메인 객체인 Drug로 변환하는 어댑터 클래스입니다.
 *
 * @since 2025-04-22
 * @modified 2025-04-24
 */
@Component
@RequiredArgsConstructor
public class GovDrugRawDataAdapter implements GovDrugRawDataPort {
    private final RawDataJpaRepository rawDataJpaRepository;

    /**
     * 마지막으로 처리된 시퀀스 이후의 원시 데이터를 페이징 조건에 맞춰 조회하고
     * Drug 도메인 리스트로 변환하여 반환합니다.
     *
     * @param lastSeq  마지막 처리 시퀀스 (null이면 0부터 조회)
     * @param pageable 페이징 및 정렬 정보
     * @return Drug 도메인 객체 리스트
     * @throws IndexException 데이터베이스 조회 실패 시 발생
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    @Override
    public List<Drug> fetchRawData(Long lastSeq, Pageable pageable) {
        long startSeq = getStartSeq(lastSeq);
        List<GovDrugRawDataEntity> govDrugRawDataEntities = getGovDrugRawDataEntities(startSeq, pageable);

        return convertToDrugDomains(govDrugRawDataEntities);
    }

    /**
     * lastSeq가 null일 경우 0으로 치환하여 조회 시작점을 결정합니다.
     *
     * @param lastSeq 마지막 처리 시퀀스
     * @return 실제 조회 시작 시퀀스
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private Long getStartSeq(Long lastSeq) {
        return (lastSeq == null ? 0L : lastSeq);
    }

    /**
     * JPA 레포지토리를 이용해 itemSeq 기준으로 정렬된 데이터를 조회합니다.
     *
     * @param lastSeq 마지막으로 조회된 Seq
     * @param pageable 페이징 및 정렬 정보
     * @return 조회된 GovDrugRawDataEntity 리스트
     * @throws IndexException 조회 중 예외가 발생하면 SearchErrorCode.RAW_DATA_FETCH_ERROR로 래핑하여 던집니다.
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private List<GovDrugRawDataEntity> getGovDrugRawDataEntities(Long lastSeq, Pageable pageable) {
        try {
            return rawDataJpaRepository.findByItemSeqGreaterThanOrderByItemSeqAsc(lastSeq, pageable);
        } catch (Exception e) {
            //TODO: LOG ERROR 처리 요망
//            log(LogLevel.ERROR, "MySQL 데이터 조회 실패", e);
            throw new IndexException(IndexErrorCode.RAW_DATA_FETCH_ERROR);
        }

    }

    /**
     * 조회된 엔티티 리스트를 Drug 도메인 객체 리스트로 변환합니다.
     *
     * @param rawData GovDrugRawDataEntity 리스트
     * @return Drug 도메인 객체 리스트
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private List<Drug> convertToDrugDomains(List<GovDrugRawDataEntity> rawData) {
        return rawData.stream()
                .map(this::mapToDrugDomain)
                .collect(Collectors.toList());
    }

    /**
     * 단일 GovDrugRawDataEntity를 Drug 도메인 객체로 매핑합니다.
     *
     * @param entity 변환할 GovDrugRawDataEntity
     * @return 변환된 Drug 도메인 객체
     * @author 정안식
     * @since 2025-04-22
     * @modified 2025-04-24
     */
    private Drug mapToDrugDomain(GovDrugRawDataEntity entity) {
        return Drug.builder()
                .itemSeq(entity.getItemSeq())
                .itemName(entity.getItemName())
                .entpName(entity.getEntpName())
                .eeText(entity.getEeDocData())
                .build();
    }
}