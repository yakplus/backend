package com.likelion.backendplus4.yakplus.search.domain.model;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.search.common.exception.SearchException;
import com.likelion.backendplus4.yakplus.search.common.exception.error.SearchErrorCode;
import lombok.Builder;
import lombok.Getter;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;
@Getter
@Builder
public class DrugSearchNatural {
    private final String query;
    private final int page;
    private final int size;

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
