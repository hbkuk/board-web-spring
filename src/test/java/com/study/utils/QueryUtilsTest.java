package com.study.utils;

import com.study.ebsoft.exception.SearchConditionException;
import com.study.ebsoft.utils.SearchConditionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class QueryUtilsTest {

    @Nested
    @DisplayName("buildQueryCondition")
    class BuildQueryCondition {

        @ParameterizedTest
        @CsvSource(value = {"20201010:2020-10-10", "20220120:2022-01-20", "19991010:1999-10-10"}, delimiter = ':')
        @DisplayName("전달된 문자열이 {yyMMdd} 형식이라면 {yy-MM-dd} 포맷팅하고 리턴한다. ")
        void valid_start_date(String input, String formattedInput) {
            String forMattedDate = SearchConditionUtils.formattedDate(input);
            assertThat(forMattedDate).isEqualTo(formattedInput);
        }

        @ParameterizedTest
        @ValueSource(strings = {"2020-1010", "2020", "'drop", "select"})
        @DisplayName("전달된 문자열이 {yyMMdd} 형식이 아니라면 SearchConditionException 예외를 던진다.")
        void invalid_start_date_(String input) {
            assertThatExceptionOfType(SearchConditionException.class)
                    .isThrownBy(() -> {
                        SearchConditionUtils.formattedDate(input);
                    }).withMessageMatching("올바른 날짜 형식이 아닙니다");
        }

        @Test
        @DisplayName("시작 날짜(startDate)가 null이면 현재날짜에서 1년 전 날짜를 {yyyy-MM-dd}포맷팅 후 리턴한다")
        void start_date_when_null() {
            // given
            String startDate = null;

            // when
            String forMattedStartDate = SearchConditionUtils.formatCustomStartDate(startDate);

            // then
            assertThat(forMattedStartDate).isEqualTo(LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        @Test
        @DisplayName("종료 날짜(startDate)가 null이면 현재날짜를 {yyyy-MM-dd}포맷팅 후 리턴한다")
        void end_date_when_null() {
            // given
            String endDate = null;

            // when
            String forMattedEndDate = SearchConditionUtils.formatCustomEndDate(endDate);

            // then
            assertThat(forMattedEndDate).isEqualTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

    }
// 수정 후 주석해제.
//    @Nested
//    @DisplayName("buildQueryString")
//    class BuildQueryString {
//
//        @Test
//        @DisplayName("검색 검색조건에 해당하는 파라미터가 없을 경우")
//        void notting_query_string() {
//            Map<String, String[]> parameters = new HashMap<>();
//
//            String queryString = SearchConditionUtils.buildQueryString(parameters);
//            assertThat(queryString.toString()).isEqualTo("");
//        }
//
//        @Test
//        @DisplayName("시작 날짜만 가지고 있을 경우")
//        void only_start_date() {
//            Map<String, String[]> parameters = new HashMap<>();
//            parameters.put("start_date", new String[]{"20230511"});
//
//            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//            assertThat(queryBuilder.toString()).isEqualTo("start_date=2023-05-11");
//        }
//
//        @Test
//        @DisplayName("종료 날짜만 가지고 있을 경우")
//        void only_ned_date() {
//            Map<String, String[]> parameters = new HashMap<>();
//            parameters.put("end_date", new String[]{"20230511"});
//
//            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//            assertThat(queryBuilder.toString()).isEqualTo("end_date=2023-05-11");
//        }
//
//        @Test
//        @DisplayName("검색 조건만 가지고 있을 경우")
//        void only_search_query() {
//            Map<String, String[]> parameters = new LinkedHashMap<>();
//            parameters.put("keyword", new String[]{"소프트웨어 장인"});
//
//            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//            assertThat(queryBuilder.toString()).isEqualTo("keyword=소프트웨어 장인");
//        }
//
//        @Test
//        @DisplayName("카테고리만 가지고 있을 경우")
//        void only_category() {
//            Map<String, String[]> parameters = new LinkedHashMap<>();
//            parameters.put("category_idx", new String[]{"1"});
//
//            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//            assertThat(queryBuilder.toString()).isEqualTo("category_idx=1");
//        }
//    }
//
//    @Test
//    @DisplayName("시작 날짜와 종료 날짜만 가지고 있을 경우")
//    void start_date_and_end_date() {
//        Map<String, String[]> parameters = new LinkedHashMap<>();
//        parameters.put("start_date", new String[]{"20230511"});
//        parameters.put("end_date", new String[]{"20230520"});
//
//        String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//        assertThat(queryBuilder).isEqualTo("start_date=2023-05-11&end_date=2023-05-20");
//    }
//
//    @Test
//    @DisplayName("종료 날짜와 검색 조건만 가지고 있는 경우")
//    void start_date_and_end_date_and_search_query() {
//        Map<String, String[]> parameters = new LinkedHashMap<>();
//        parameters.put("end_date", new String[]{"20230520"});
//        parameters.put("keyword", new String[]{"소프트웨어 장인"});
//
//        String queryBuilder = SearchConditionUtils.buildQueryString(parameters);
//
//        assertThat(queryBuilder.toString()).isEqualTo("end_date=2023-05-20&keyword=소프트웨어 장인");
//    }
//
//    @Test
//    @DisplayName("시작 날짜보다 종료 날짜가 크다면 예외를 던진다")
//    void throw_exception_when_start_date_bigger_then_end_date() {
//        Map<String, String[]> parameters = new LinkedHashMap<>();
//        parameters.put("end_date", new String[]{"2023-05-11"});
//        parameters.put("start_date", new String[]{"2023-05-20"});
//
//        assertThatExceptionOfType(SearchConditionException.class)
//                .isThrownBy(() -> {
//                    SearchConditionUtils.buildQueryString(parameters);
//                }).withMessageMatching("시작 날짜보다 종료 날짜가 클 수 없습니다.");
//    }
}
