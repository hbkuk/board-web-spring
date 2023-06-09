package com.study.ebsoft.utils;

import com.study.ebsoft.exception.SearchConditionException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * 웹 브라우저가 전송한 파리미터에서 검색조건(Search Condition) 파라미터를 추출해서
 * SQL 쿼리(Query) 또는 쿼리 스트링(Query String) 으로 반환하는 유틸 클래스
 */
@Slf4j
public class SearchConditionUtils {

    /**
     * 파라미터 맵에서 검색조건에 해당하는 파라미터 이름(name)
     */
    public static final String START_DATE_PARAMETER_KEY = "start_date";
    public static final String END_DATE_PARAMETER_KEY = "end_date";
    public static final String CATEGORY_IDX_PARAMETER_KEY = "category_idx";
    public static final String KEYWORD_PARAMETER_KEY = "keyword";

    private static final String[] SEARCH_CONDITIONS =
            {START_DATE_PARAMETER_KEY, END_DATE_PARAMETER_KEY,
                    KEYWORD_PARAMETER_KEY, CATEGORY_IDX_PARAMETER_KEY};

    private static final int DEFAULT_MINUS_YEARS = 1;
    private static final String INPUT_DATE_FORMAT = "yyyyMMdd";
    private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 파라미터 맵에서 쿼리 스트링(Query String)을 생성하여 문자열을 반환합니다.
     *
     * @param parameterMap 쿼리 스트링에 사용되는 파라미터 맵입니다.
     */
    public static String buildQueryString(Map<String, String[]> parameterMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!hasLeastOneSearchCondition(parameterMap)) {
            return "";
        }

        if (isEmptyBothDate(parameterMap)) {
            isDateRange(parameterMap);
        }

        for (String key : parameterMap.keySet()) {
            log.debug("Build Query String Key : {} ", key);
            for (String searchCondition : SEARCH_CONDITIONS) {
                if (key.equals(searchCondition)) {
                    if(key.equals(START_DATE_PARAMETER_KEY) || key.equals(END_DATE_PARAMETER_KEY) ) {
                        queryBuilder.append(key).append("=").append(formatCustomStartDate(parameterMap.get(key)[0]));
                        log.debug("Build Query String append : {} ", queryBuilder);
                        queryBuilder.append("&");
                        break;
                    } else {
                        queryBuilder.append(key).append("=").append(cleanInput(parameterMap.get(key)[0]));
                        log.debug("Build Query String append : {} ", queryBuilder);
                        queryBuilder.append("&");
                        break;
                    }
                }
            }
        }
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());

        return queryBuilder.toString();
    }

    /**
     * 전달된 문자열에서 알파벳, 숫자, 한글 이외의 모든 문자를 공백으로 치환하여 리턴합니다
     *
     * Examples:
     *      1. {Hello World!} -> {Hello World }
     *      2. {!@#$%^&*()} -> {          }
     *      3. {SELECT * FROM users WHERE username = '' OR ''='' --}
     *                              -> {SELECT   FROM users WHERE username     OR       }
     *
     * @param input 입력 문자열
     * @return 공백으로 치환된 문자열
     */
    public static String cleanInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9가-힣]", " ");
    }

    /**
     * 전달된 날짜 형식의 문자열{yyyyMMdd}을 커스텀 형식{yyyy-MM-dd}으로 포맷팅하고 리턴합니다.
     * 만약 입력된 날짜 문자열이 없는 경우, 기본 날짜를 사용하여 포맷팅합니다.
     *
     * @param inputDateStr 포맷팅할 날짜 문자열
     * @return 포맷팅된 날짜 문자열
     * @throws SearchConditionException 날짜 형식이 올바르지 않은 경우 발생합니다
     */
    public static String formatCustomStartDate(String inputDateStr) {
        if(inputDateStr == null) {
            return LocalDate.now().minusYears(DEFAULT_MINUS_YEARS).format(DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT));
        }
        return formattedDate(inputDateStr);
    }

    /**
     * 전달된 날짜 형식의 문자열{yyyyMMdd}을 커스텀 형식{yyyy-MM-dd}으로 포맷팅하고 리턴합니다.
     * 만약 입력된 날짜 문자열이 없는 경우, 현재 날짜를 사용하여 포맷팅합니다.
     *
     * @param inputDateStr 포맷팅할 날짜 문자열
     * @return 포맷팅된 날짜 문자열
     * @throws SearchConditionException 날짜 형식이 올바르지 않은 경우 발생합니다
     */
    public static String formatCustomEndDate(String inputDateStr) {
        if(inputDateStr == null) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT));
        }

        return formattedDate(inputDateStr);
    }

    /**
     * 날짜 문자열을 커스텀 형식으로 포맷팅합니다.
     *
     * @param inputDateStr 포맷팅할 날짜 문자열
     * @return 포맷팅된 날짜 문자열
     * @throws SearchConditionException 날짜 형식이 올바르지 않은 경우 발생합니다
     */
    public static String formattedDate(String inputDateStr) {
        try {
            LocalDate date = LocalDate.parse(inputDateStr, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
            return date.format(DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new SearchConditionException("올바른 날짜 형식이 아닙니다");
        }
    }

    /**
     * 파라미터 맵이 적어도 하나의 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 파라미터 맵입니다.
     * @return 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean hasLeastOneSearchCondition(Map<String, String[]> parameterMap) {
        boolean isSearchCondition = false;

        for (String searchCondition : SEARCH_CONDITIONS) {
            if (parameterMap.get(searchCondition) != null && parameterMap.get(searchCondition)[0] != null) {
                isSearchCondition = true;
            }
        }
        return isSearchCondition;
    }

    /**
     * 파라미터 맵이 시작일과 종료일을 가지는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 파라미터 맵입니다.
     * @return 시작일과 종료일을 가지는 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isEmptyBothDate(Map<String, String[]> parameterMap) {
        return parameterMap.get(START_DATE_PARAMETER_KEY) != null && parameterMap.get(END_DATE_PARAMETER_KEY) != null;
    }

    /**
     * 시작일이 종료일보다 큰 경우, 예외를 발생시킵니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 파라미터 맵입니다.
     */
    private static void isDateRange(Map<String, String[]> parameterMap) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(parameterMap.get(START_DATE_PARAMETER_KEY)[0]);
            endDate = LocalDate.parse(parameterMap.get(END_DATE_PARAMETER_KEY)[0]);
        } catch (DateTimeParseException e) {
            throw new SearchConditionException("날짜 형식이 아닙니다.");
        }
        if (startDate.isAfter(endDate)) {
            throw new SearchConditionException("시작 날짜보다 종료 날짜가 클 수 없습니다.");
        }
    }
}
