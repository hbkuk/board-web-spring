package com.study.ebsoft.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 검색조건을 나타내는 클래스입니다.
 */
@Data
public class SearchCondition {

    /**
     * 시작 날짜입니다.
     */
    @Nullable
    @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "시작날짜 형식이 잘못되었습니다")
    private String startDate;

    /**
     * 종료 날짜입니다.
     */
    @Nullable
    @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "종료날짜 형식이 잘못되었습니다")

    private String endDate;

    /**
     * 카테고리 번호입니다.
     */
    @Nullable
    @Min(value = 1, message = "카테고리 번호는 1보다 큰 숫자여야 합니다")
    private Integer categoryIdx;

    /**
     * 검색 키워드입니다.
     */
    @Nullable
    @Size(min = 4, max = 100, message = "키워드는 4글자 이상, 100글자 이하여야 합니다")
    private String keyword;

    /**
     * 페이지 번호입니다.
     */
    @Nullable
    @Min(value = 1, message = "페이지 번호는 1보다 큰 숫자여야 합니다")
    private Integer pageNo;

    /**
     * 페이지를 의미하는 객체입니다.
     */
    @Null
    private Page page;

    /**
     * SearchCondition 객체의 페이지 정보를 업데이트합니다.
     *
     * @return Page 객체
     */
    public void updatePage() {
        this.page = new Page(this.pageNo);
    }
}
