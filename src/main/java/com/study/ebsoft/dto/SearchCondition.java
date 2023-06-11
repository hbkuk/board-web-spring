package com.study.ebsoft.dto;

import lombok.*;

/**
 * 검색조건을 나타내는 클래스입니다.
 */
@Data
public class SearchCondition {

    private final String startDate;
    private final String endDate;
    private final Integer categoryIdx;
    private final String keyword;

}
