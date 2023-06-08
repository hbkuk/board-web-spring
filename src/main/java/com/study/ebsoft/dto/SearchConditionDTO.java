package com.study.ebsoft.dto;

import lombok.*;

/**
 * 검색조건을 나타내는 클래스입니다.
 */
@Builder(toBuilder = true)
@ToString
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class SearchConditionDTO {
    private final String startDate;
    private final String endDate;
    private final Integer categoryIdx;
    private final String keyword;
}
