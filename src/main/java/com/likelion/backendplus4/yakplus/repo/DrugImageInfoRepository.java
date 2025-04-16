package com.likelion.backendplus4.yakplus.repo;

import com.likelion.backendplus4.yakplus.domain.DrugImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DrugImageInfoRepository는 DrugImageInfo 엔티티에 대한 데이터 접근을 제공하는 Spring Data JPA Repository 인터페이스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
public interface DrugImageInfoRepository extends JpaRepository<DrugImageInfo, String> {
}
