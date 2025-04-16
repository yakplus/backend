package com.likelion.backendplus4.yakplus.controller;

import com.likelion.backendplus4.yakplus.service.DrugDataIngestionService;
import com.likelion.backendplus4.yakplus.service.DrugDataMergeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * DrugDataIngestionController는 데이터 수집 및 병합 관련 요청을 처리하는 컨트롤러이다.
 *
 * @since 2025-04-14 최초 작성
 * @author 정안식
 */
// TODO: 전역 예외 처리 필요
@RestController
@RequestMapping("/api/ingest")
@RequiredArgsConstructor
public class DrugDataIngestionController {

    private final DrugDataIngestionService ingestionService;
    private final DrugDataMergeService mergeService;

    /**
     * 의약품 허가정보를 수집하여 저장하는 엔드포인트.
     *
     * 동작 원리:
     * 1. DrugDataIngestionService의 ingestPermitInfo()를 호출하여 외부 Permit API로부터 데이터를 수집한다.
     * 2. 수집한 데이터를 DrugPermitInfo 및 DrugCombinedInfo에 저장한다.
     *
     * @return 성공 메시지 또는 에러 메시지
     */
    @PostMapping("/permit-info")
    public ResponseEntity<String> ingestPermitInfo() {
        try {
            ingestionService.ingestPermitInfo();
            return ResponseEntity.ok("Drug permit info ingested successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error ingesting drug permit info: " + e.getMessage());
        }
    }

    /**
     * 의약품 이미지 정보를 수집하여 저장하는 엔드포인트.
     *
     * 동작 원리:
     * 1. DrugDataIngestionService의 ingestImageInfo()를 호출하여 저장된 item_seq 목록에 대해 이미지 데이터를 수집한다.
     * 2. 수집한 이미지를 DrugImageInfo에 저장하고, DrugCombinedInfo의 이미지 URL을 업데이트한다.
     *
     * @return 성공 메시지 또는 에러 메시지
     */
    @PostMapping("/image-info")
    public ResponseEntity<String> ingestImageInfo() {
        try {
            ingestionService.ingestImageInfo();
            return ResponseEntity.ok("Drug image info ingested successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error ingesting drug image info: " + e.getMessage());
        }
    }

    /**
     * DrugPermitInfo와 DrugImageInfo를 병합하여 DrugCombinedInfo 테이블을 생성 또는 업데이트하는 엔드포인트.
     *
     * 동작 원리:
     * 1. DrugDataMergeService의 mergeDrugData()를 호출하여 저장된 데이터를 결합한다.
     *
     * @return 성공 메시지 또는 에러 메시지
     */
    @PostMapping("/merge")
    public ResponseEntity<String> mergeDrugData() {
        try {
            mergeService.mergeDrugData();
            return ResponseEntity.ok("Drug data merged successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error merging drug data: " + e.getMessage());
        }
    }
}
