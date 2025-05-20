package com.likelion.backendplus4.yakplus.search.domain.model;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import lombok.Builder;
import lombok.Getter;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 자연어 검색 요청 정보를 담는 도메인 모델 클래스
 *
 * query, page, size 필드를 통해 검색 조건을 정의하고,
 * 유효성 검증을 수행합니다.
 *
 * @since 2025-05-02
 */
@Getter
@Builder
public class DrugSearchNatural {
    private final String query;
    private final int page;
    private final int size;

    /**
     * 주어진 검색 파라미터로 객체를 생성합니다.
     * 입력값의 유효성을 검사하여 부적절한 경우 예외를 던집니다.
     *
     * @param query 검색을 위한 자연어 쿼리 문자열 (null 또는 빈 문자열 불가)
     * @param page  조회할 페이지 번호 (0 이상)
     * @param size  페이지당 결과 개수 (1 이상)
     * @throws SearchException 쿼리가 비어있거나 페이지/사이즈 값이 부적절한 경우
     * @author 정안식
     * @since 2025-05-02
     */
    public DrugSearchNatural(String query, int page, int size) {

        if (query == null || query.isBlank()) {
            log(LogLevel.ERROR, "쿼리가 null이거나 비어있습니다.");
            throw new SearchException(SearchErrorCode.INVALID_QUERY);
        }
        if (page < 0) {
            log(LogLevel.ERROR, "페이지 번호가 음수입니다.");
            throw new SearchException(SearchErrorCode.INVALID_PAGE);
        }
        if (size <= 0) {
            log(LogLevel.ERROR, "사이즈가 0 이하입니다.");
            throw new SearchException(SearchErrorCode.INVALID_SIZE);
        }
        this.query = query;
        this.page = page;
        this.size = size;
    }
}
