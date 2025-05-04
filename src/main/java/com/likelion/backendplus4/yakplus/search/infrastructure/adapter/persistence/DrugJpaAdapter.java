package com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence;


import com.likelion.backendplus4.yakplus.search.application.port.out.DrugSearchRdbRepositoryPort;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import com.likelion.backendplus4.yakplus.search.domain.model.Drug;
import com.likelion.backendplus4.yakplus.search.infrastructure.adapter.persistence.jpa.DrugJpaRepository;
import com.likelion.backendplus4.yakplus.search.infrastructure.support.DrugMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * DB에서 약품 데이터를 다루는 어댑터입니다.
 *
 * @author 박찬병
 * @fields LEADING_NUMBER_PATTERN 약품 주의사항의 키에서 선행 숫자를 추출하기 위한 정규 표현식
 * @fields drugJpaRepository      약품 정보를 저장하는 JPA 레포지토리
 * @modified 2025-05-03 함예정
 * - RDB 저장된 주의사항을 순서대로 정렬하는 기능 추가
 * @modified 2025-04-25
 * @since 2025-04-24
 */
@Component
@RequiredArgsConstructor
public class DrugJpaAdapter implements DrugSearchRdbRepositoryPort {
    private static final Pattern LEADING_NUMBER_PATTERN = Pattern.compile("^(\\d{1,2})");

    private final DrugJpaRepository drugJpaRepository;

    /**
     * 약품 정보를 DB에서 조회합니다.
     *
     * @param id 약품 ID
     * @return 약품 정보
     * @throws SearchException 약품을 찾을 수 없는 경우
     * @author 함예정
     * @since 2025-04-30
     */
    @Override
    public Drug findById(Long id) {
        Drug drug = DrugMapper.toDomainFromEntity(
                drugJpaRepository.findById(id).orElseThrow(
                        () -> new SearchException(SearchErrorCode.RDB_SEARCH_ERROR)));
        Map<String, List<String>> precaution = drug.getPrecaution();
        if (precaution != null) {
            drug.setPrecaution(sortByLeadingNumberIfPresent(precaution));
        }
        return drug;
    }

    /**
     * 약품 주의사항을 선행 숫자에 따라 정렬합니다.
     * @param input 약품 주의사항 Map
     * @return Map  정렬된 약품 주의사항 Map
     * @since 2025-05-03
     * @author 함예정
     */
    private Map<String, List<String>> sortByLeadingNumberIfPresent(Map<String, List<String>> input) {
        return input.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> extractLeadingNumberOrMax(entry.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * 약품 주의사항의 키에서 선행 숫자를 추출합니다.
     * 만약 숫자가 없으면 Integer.MAX_VALUE를 반환합니다.
     * 숫자 포맷에 오류가 발생하면 예외를 무시하고 Integer.MAX_VALUE를 반환합니다.
     *
     * @param key 약품 주의사항의 키
     * @return 선행 숫자 또는 Integer.MAX_VALUE
     */
    private int extractLeadingNumberOrMax(String key) {
        Matcher matcher = LEADING_NUMBER_PATTERN.matcher(key);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {}
        }
        return Integer.MAX_VALUE;
    }
}
