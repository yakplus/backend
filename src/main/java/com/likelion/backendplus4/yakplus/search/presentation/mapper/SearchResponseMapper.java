package com.likelion.backendplus4.yakplus.search.presentation.mapper;

import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomain;
import com.likelion.backendplus4.yakplus.search.domain.model.DrugSearchDomainNatural;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.DrugMapper;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponse;
import com.likelion.backendplus4.yakplus.search.presentation.controller.dto.response.SearchResponseList;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DrugSearchDomainNatural 도메인 모델을 API 응답 DTO(SearchResponse, SearchResponseList)로 매핑하는 유틸 클래스입니다.
 *
 * @modified 2025-05-04
 * @since 2025-04-22
 */
public class SearchResponseMapper {

    /**
     * 단일 도메인 객체를 SearchResponse DTO로 변환합니다.
     *
     * @param dsn 변환할 DrugSearchDomainNatural 도메인 객체
     * @return 변환된 SearchResponse DTO
     * @author 정안식
     * @modified 2025-05-03
     * @since 2025-04-22
     */
    public static SearchResponse toResponse(DrugSearchDomainNatural dsn) {
        return SearchResponse.builder()
                .drugId(dsn.getDrugId())
                .drugName(dsn.getDrugName())
                .company(dsn.getCompany())
                .efficacy(dsn.getEfficacy())
                .imageUrl(dsn.getImageUrl())
                .build();
    }

    /**
     * 도메인 객체 리스트를 SearchResponse DTO 리스트로 변환합니다.
     *
     * @param drugs 변환할 DrugSearchDomainNatural 도메인 객체 리스트
     * @return 변환된 SearchResponse DTO 리스트
     * @author 정안식
     * @modified 2025-05-03
     * @since 2025-04-22
     */
    public static List<SearchResponse> toResponseList(List<DrugSearchDomainNatural> drugs) {
        return drugs.stream()
                .map(SearchResponseMapper::toResponse)
                .toList();
    }

    /**
     * 도메인 객체 리스트를 SearchResponseList DTO로 변환하고, 전체 결과 개수를 포함합니다.
     *
     * @param drugs 변환할 DrugSearchDomainNatural 도메인 객체 리스트
     * @return 응답 DTO(SearchResponseList) — 리스트와 총 개수를 포함
     * @author 정안식
     * @modified 2025-05-03
     * @since 2025-04-22
     */
    public static SearchResponseList toResponseListWithCount(List<DrugSearchDomainNatural> drugs) {
        List<SearchResponse> responses = toResponseList(drugs);
        return new SearchResponseList(responses, responses.size());
    }


    /**
     * DrugSearchDomain 페이지 객체를 SearchResponseList DTO로 변환합니다.
     * <p>
     * 1) 페이지에서 콘텐츠 리스트를 꺼내고,
     * 2) 각각을 DTO로 변환한 후,
     * 3) 총 개수와 함께 SearchResponseList로 래핑합니다.
     *
     * @param page Elasticsearch 검색 결과 페이지 (DrugSearchDomain 기준)
     * @return SearchResponseList DTO
     * @author 박찬병
     * @since 2025-05-03
     */
    public static SearchResponseList toResponseByKeywordDomain(Page<DrugSearchDomain> page) {
        List<SearchResponse> content = page.getContent().stream()
                .map(DrugMapper::toResponse)
                .toList();

        return new SearchResponseList(content, page.getTotalElements());
    }
}
