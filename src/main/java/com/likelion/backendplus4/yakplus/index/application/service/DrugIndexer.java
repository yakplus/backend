// package com.likelion.backendplus4.yakplus.index.application.service;
//
// import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
// import com.likelion.backendplus4.yakplus.index.application.port.out.DrugIndexRepositoryPort;
// import com.likelion.backendplus4.yakplus.index.application.port.out.GovDrugRawDataPort;
// import com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request.IndexRequest;
// import com.likelion.backendplus4.yakplus.index.domain.model.Drug;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
//
// /**
//  * 약품 색인(인덱싱) 작업을 수행하는 서비스 구현체
//  *
//  * @since 2025-04-22
//  * @modified 2025-04-24
//  */
// @Service
// @RequiredArgsConstructor
// public class DrugIndexer implements IndexUseCase {
//     private final GovDrugRawDataPort govDrugRawDataPort;
//     private final DrugIndexRepositoryPort drugIndexRepositoryPort;
//     private static final String SORT_BY_PROPERTY = "itemSeq";
//
//     /**
//      * 요청으로 전달된 lastSeq, limit 정보를 바탕으로 RDB에서 데이터를 조회하고
//      * ES 인덱스에 저장한다.
//      *
//      * @param request 색인 기준 및 개수 정보
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     @Override
//     public void index(IndexRequest request) {
//         Pageable pageable = createPageable(request.limit());
//         List<Drug> drugs = fetchAndTransformRawData(request, pageable);
//         saveDrugs(drugs);
//     }
//
//     /**
//      * RDB에서 lastSeq 이후의 원시 데이터를 조회하여 도메인 객체로 변환한다.
//      *
//      * @param request 색인 기준 정보
//      * @param pageable 페이징 및 정렬 정보
//      * @return 도메인 모델 리스트
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     private List<Drug> fetchAndTransformRawData(IndexRequest request, Pageable pageable) {
//         return govDrugRawDataPort.fetchRawData(request.lastSeq(), pageable);
//     }
//
//     /**
//      * limit 크기 및 itemSeq 오름차순 정렬 기준의 객체를 생성한다.
//      *
//      * @param limit 조회할 최대 건수
//      * @return 페이징 객체
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     private Pageable createPageable(int limit) {
//         return PageRequest.of(0, limit, Sort.by(SORT_BY_PROPERTY).ascending());
//     }
//
//     /**
//      * 조회된 도메인 객체들을 ES 인덱스에 저장 처리한다.
//      *
//      * @param drugs 저장할 도메인 모델 리스트
//      * @author 정안식
//      * @since 2025-04-22
//      * @modified 2025-04-24
//      */
//     private void saveDrugs(List<Drug> drugs) {
//         drugIndexRepositoryPort.saveAll(drugs);
//     }
// }