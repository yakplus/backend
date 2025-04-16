package com.likelion.backendplus4.yakplus.repo;

import com.likelion.backendplus4.yakplus.domain.DrugPermitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * DrugPermitInfoRepository는 DrugPermitInfo 엔티티에 대한 데이터 접근을 제공하는 Spring Data JPA Repository 인터페이스이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
public interface DrugPermitInfoRepository extends JpaRepository<DrugPermitInfo, String> {

    @Query("SELECT d.itemSeq FROM DrugPermitInfo d")
    List<String> findAllItemSeqs();
}
