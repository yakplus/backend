package com.likelion.backendplus4.yakplus.search.application.port.out;

import com.likelion.backendplus4.yakplus.search.domain.model.Drug;

/**
 * RDB 기반 의약품 검색을 위한 포트 인터페이스입니다.
 * 데이터베이스로부터 의약품 정보를 조회하는 기능을 정의합니다.
 *
 * @since 2025-04-30
 */
public interface DrugSearchRdbRepositoryPort {
    /**
     * ID를 기반으로 단일 의약품 정보를 조회합니다.
     *
     * @param id 조회할 의약품의 고유 ID
     * @return 조회된 의약품 객체
     * @author 함예정
     * @since 2025-04-30
     */
    Drug findById(Long id);
}
