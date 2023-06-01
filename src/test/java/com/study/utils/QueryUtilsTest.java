package com.study.utils;

import com.study.ebsoft.exception.SearchConditionException;
import com.study.ebsoft.utils.SearchConditionUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class QueryUtilsTest {

    @Nested
    @DisplayName("buildQueryCondition")
    class BuildQueryCondition {

        @Test
        @DisplayName("검색 검색조건에 해당하는 파라미터가 없을 경우")
        void notting_query_string() {
            Map<String, String[]> parameters = new HashMap<>();

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);
            assertThat(queryBuilder).isNull();
        }

        @Test
        @DisplayName("시작 날짜만 가지고 있을 경우")
        void only_start_date() {
            Map<String, String[]> parameters = new HashMap<>();
            parameters.put("start_date", new String[]{"2023-05-11"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE DATE(b.regdate) >= '2023-05-11'");
        }

        @Test
        @DisplayName("종료 날짜만 가지고 있을 경우")
        void only_ned_date() {
            Map<String, String[]> parameters = new HashMap<>();
            parameters.put("end_date", new String[]{"2023-05-11"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE DATE(b.regdate) <= '2023-05-11'");
        }


        @Test
        @DisplayName("검색 조건만 가지고 있을 경우")
        void only_search_query() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("keyword", new String[]{"소프트웨어 장인"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE b.title LIKE '%소프트웨어 장인%' OR b.writer LIKE '%소프트웨어 장인%' OR b.content LIKE '%소프트웨어 장인%'");
        }

        @Test
        @DisplayName("카테고리만 가지고 있을 경우")
        void only_category() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("category_idx", new String[]{"1"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE c.category_idx = 1");
        }

        @Test
        @DisplayName("시작 날짜와 종료 날짜만 가지고 있을 경우")
        void start_date_and_end_date() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("start_date", new String[]{"2023-05-11"});
            parameters.put("end_date", new String[]{"2023-05-20"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE DATE(b.regdate) >= '2023-05-11' AND DATE(b.regdate) <= '2023-05-20'");
        }

        @Test
        @DisplayName("종료 날짜와 시작 날짜를 가지고 있는 경우")
        void end_date_and_start_date() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("end_date", new String[]{"2023-05-20"});
            parameters.put("start_date", new String[]{"2023-05-11"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE DATE(b.regdate) <= '2023-05-20' AND DATE(b.regdate) >= '2023-05-11'");
        }


        @Test
        @DisplayName("종료 날짜와 검색 조건만 가지고 있는 경우")
        void start_date_and_end_date_and_search_query() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("end_date", new String[]{"2023-05-20"});
            parameters.put("keyword", new String[]{"소프트웨어 장인"});

            String queryBuilder = SearchConditionUtils.buildQueryCondition(parameters);

            assertThat(queryBuilder.toString()).isEqualTo(" WHERE DATE(b.regdate) <= '2023-05-20' AND b.title LIKE '%소프트웨어 장인%' OR b.writer LIKE '%소프트웨어 장인%' OR b.content LIKE '%소프트웨어 장인%'");
        }

        @Test
        @DisplayName("시작 날짜보다 종료 날짜가 크다면 예외를 던진다")
        void throw_exception_when_start_date_bigger_then_end_date() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("end_date", new String[]{"2023-05-11"});
            parameters.put("start_date", new String[]{"2023-05-20"});

            assertThatExceptionOfType(SearchConditionException.class)
                    .isThrownBy(() -> {
                        SearchConditionUtils.buildQueryCondition(parameters);
                    }).withMessageMatching("시작 날짜보다 종료 날짜가 클 수 없습니다.");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "'Hello World!', 'Hello World '",
                "'12345', '12345'",
                "'안녕하세요!', '안녕하세요 '",
                "'!@#$%^&*()', '          '",
                "'abc123!@#', 'abc123   '"
        }, delimiter = ',')
        @DisplayName("한글, 알파벳, 숫자를 제외한 모든 문자열은 공백으로 치환된다")
        void replaceAll(String actual, String expected) {
            assertThat(SearchConditionUtils.cleanInput(actual)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "'SELECT * FROM users WHERE username = '' OR ''='' --','SELECT   FROM users WHERE username     OR       '",
                "'SELECT * FROM users; DROP TABLE users;', 'SELECT   FROM users  DROP TABLE users '",
        }, delimiter = ',')
        @DisplayName("한글, 알파벳, 숫자를 제외한 모든 문자열은 공백으로 치환된다")
        void sqlInjection_replaceAll(String actual, String expected) {
            assertThat(SearchConditionUtils.cleanInput(actual)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"' OR '1'='1", "'; DROP TABLE users;--", "1'; SELECT * FROM users;--"})
        @DisplayName("날짜 형식으로 변환할때 인젝션에 해당하는 문자열이 전달된다면 예외가 발생한다.")
        void throw_exception_when_not_date_format(String value) {
            assertThatExceptionOfType(SearchConditionException.class)
                    .isThrownBy(() -> {
                        SearchConditionUtils.formatDate(value);})
                    .withMessageMatching("올바른 날짜 형식이 아닙니다");
        }
    }

    @Nested
    @DisplayName("buildQueryString")
    class BuildQueryString {

        @Test
        @DisplayName("검색 검색조건에 해당하는 파라미터가 없을 경우")
        void notting_query_string() {
            Map<String, String[]> parameters = new HashMap<>();

            String queryString = SearchConditionUtils.buildQueryString(parameters);
            assertThat(queryString.toString()).isEqualTo("");
        }

        @Test
        @DisplayName("시작 날짜만 가지고 있을 경우")
        void only_start_date() {
            Map<String, String[]> parameters = new HashMap<>();
            parameters.put("start_date", new String[]{"2023-05-11"});

            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

            assertThat(queryBuilder.toString()).isEqualTo("start_date=2023-05-11");
        }

        @Test
        @DisplayName("종료 날짜만 가지고 있을 경우")
        void only_ned_date() {
            Map<String, String[]> parameters = new HashMap<>();
            parameters.put("end_date", new String[]{"2023-05-11"});

            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

            assertThat(queryBuilder.toString()).isEqualTo("end_date=2023-05-11");
        }

        @Test
        @DisplayName("검색 조건만 가지고 있을 경우")
        void only_search_query() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("keyword", new String[]{"소프트웨어 장인"});

            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

            assertThat(queryBuilder.toString()).isEqualTo("keyword=소프트웨어 장인");
        }

        @Test
        @DisplayName("카테고리만 가지고 있을 경우")
        void only_category() {
            Map<String, String[]> parameters = new LinkedHashMap<>();
            parameters.put("category_idx", new String[]{"1"});

            String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

            assertThat(queryBuilder.toString()).isEqualTo("category_idx=1");
        }
    }

    @Test
    @DisplayName("시작 날짜와 종료 날짜만 가지고 있을 경우")
    void start_date_and_end_date() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("start_date", new String[]{"2023-05-11"});
        parameters.put("end_date", new String[]{"2023-05-20"});

        String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("start_date=2023-05-11&end_date=2023-05-20");
    }

    @Test
    @DisplayName("종료 날짜와 검색 조건만 가지고 있는 경우")
    void start_date_and_end_date_and_search_query() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("end_date", new String[]{"2023-05-20"});
        parameters.put("keyword", new String[]{"소프트웨어 장인"});

        String queryBuilder = SearchConditionUtils.buildQueryString(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("end_date=2023-05-20&keyword=소프트웨어 장인");
    }

    @Test
    @DisplayName("시작 날짜보다 종료 날짜가 크다면 예외를 던진다")
    void throw_exception_when_start_date_bigger_then_end_date() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("end_date", new String[]{"2023-05-11"});
        parameters.put("start_date", new String[]{"2023-05-20"});

        assertThatExceptionOfType(SearchConditionException.class)
                .isThrownBy(() -> {
                    SearchConditionUtils.buildQueryString(parameters);
                }).withMessageMatching("시작 날짜보다 종료 날짜가 클 수 없습니다.");
    }
}
