package com.study.ebsoft.utils;

import com.study.ebsoft.exception.SearchConditionException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
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
    private static final String START_DATE_PARAMETER_KEY = "start_date";
    private static final String END_DATE_PARAMETER_KEY = "end_date";
    private static final String CATEGORY_IDX_PARAMETER_KEY = "category_idx";
    private static final String KEYWORD_PARAMETER_KEY = "keyword";

    /**
     * 각각의 검색조건에 해당하는 SQL query Format
     */
    private static final String START_DATE_CONDITION_QUERY = "DATE(b.regdate) >= '%s'";
    private static final String END_DATE_CONDITION_QUERY = "DATE(b.regdate) <= '%s'";
    private static final String CATEGORY_CONDITION_QUERY = "c.category_idx = %d";
    private static final String KEYWORD_CONDITION_QUERY = "b.title LIKE '%%%s%%' OR b.writer LIKE '%%%s%%' OR b.content LIKE '%%%s%%'";

    private static final int REMOVE_LENGTH_WHEN_COMPLETE_QUERY = 5;
    private static final String[] SEARCH_CONDITIONS =
            {START_DATE_PARAMETER_KEY, END_DATE_PARAMETER_KEY,
                    KEYWORD_PARAMETER_KEY, CATEGORY_IDX_PARAMETER_KEY};
    
    private static final int DEFAULT_MINUS_YEARS = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
                        queryBuilder.append(key).append("=").append(formatDate(parameterMap.get(key)[0]));
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
     * 파라미터 맵에서 쿼리(Query)를 생성하여 문자열을 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 파라미터 맵입니다.
     */
    public static String buildQueryCondition(Map<String, String[]> parameterMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!hasLeastOneSearchCondition(parameterMap)) {
            return null;
        }

        if (isEmptyBothDate(parameterMap)) {
            isDateRange(parameterMap);
        }

        queryBuilder.append(" WHERE ");
        for (String paramName : parameterMap.keySet()) {
            if (isKeyWord(paramName)) {
                String value = cleanInput(parameterMap.get(paramName)[0]);
                queryBuilder.append(
                        String.format(KEYWORD_CONDITION_QUERY, value, value, value));
                queryBuilder.append(" AND ");
            }

            if (isCategory(paramName)) {
                String value = cleanInput(parameterMap.get(paramName)[0]);
                queryBuilder.append(
                        String.format(CATEGORY_CONDITION_QUERY, Integer.parseInt(value)));
                queryBuilder.append(" AND ");
            }

            if (isStartDate(paramName)) {
                String value = formatDate(parameterMap.get(paramName)[0]);
                queryBuilder.append(String.format(START_DATE_CONDITION_QUERY, value));
                queryBuilder.append(" AND ");
            }

            if (isEndDate(paramName)) {
                String value = formatDate(parameterMap.get(paramName)[0]);
                queryBuilder.append(String.format(END_DATE_CONDITION_QUERY, value));
                queryBuilder.append(" AND ");
            }
            log.debug("Query Builder Append" + queryBuilder);
        }
        queryBuilder.delete(queryBuilder.length() - REMOVE_LENGTH_WHEN_COMPLETE_QUERY, queryBuilder.length());
        log.debug("Result Query Builder" + queryBuilder);
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
     * 전달된 날짜 형식의 문자열을 {yyyy-MM-dd} 형식으로 포맷팅하고 리턴합니다
     *
     * @param dateStr 포맷팅할 날짜 문자열
     * @return 포맷팅된 날짜 문자열
     * @throws IllegalArgumentException 만약 날짜 형식이 아닌 경우 발생합니다
     */
    public static String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
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
     * 매개변수 이름이 키워드를 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param paramName 매개변수의 이름
     * @return 매개변수 이름이 키워드 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isKeyWord(String paramName) {
        return paramName.equals(KEYWORD_PARAMETER_KEY);
    }

    /**
     * 매개변수 이름이 카테고리를 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param paramName 매개변수의 이름
     * @return 매개변수 이름이 검색 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isCategory(String paramName) {
        return paramName.equals(CATEGORY_IDX_PARAMETER_KEY);
    }

    /**
     * 매개변수 이름이 시작 날짜 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param paramName 매개변수의 이름
     * @return 매개변수 이름이 시작 날짜 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isStartDate(String paramName) {
        return paramName.equals(START_DATE_PARAMETER_KEY);
    }

    /**
     * 매개변수의 이름이 종료 날짜 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param paramName 매개변수의 이름
     * @return 매개변수 이름이 종료 날짜 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isEndDate(String paramName) {
        return paramName.equals(END_DATE_PARAMETER_KEY);
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

    /**
     * 요청 파라미터에 시작 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 {yyyy-mm-dd} 형태의 문자열로 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 시작 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 반환
     */
    public static String getParamStartDate(HttpServletRequest request) {
        LocalDate defaultStartDate = LocalDate.now().minusYears(DEFAULT_MINUS_YEARS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String startDate = defaultStartDate.format(formatter);

        if (request.getParameter(START_DATE_PARAMETER_KEY) != null) {
            startDate = request.getParameter(START_DATE_PARAMETER_KEY);
        }
        return startDate;
    }

    /**
     * 요청 파라미터에 종료 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 {yyyy-mm-dd} 형태의 문자열로 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 종료 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 반환
     */
    public static String getParamEndDate(HttpServletRequest request) {
        LocalDate defaultStartDate =  LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String end_date = defaultStartDate.format(formatter);

        if (request.getParameter(END_DATE_PARAMETER_KEY) != null) {
            end_date = request.getParameter(END_DATE_PARAMETER_KEY);
        }
        return end_date;
    }

    /**
     * 요청 파라미터에 검색 키워드가 있는 경우 그 값을, 아니라면 빈 문자열("")을 반환한다.
     * 
     * @param request 요청 정보를 담고있는 객체
     * @return 검색 키워드가 있는 경우 그 값을, 아니라면 빈 문자열("")을 반환
     */
    public static String getParamKeyword(HttpServletRequest request) {
        if (request.getParameter(KEYWORD_PARAMETER_KEY) != null) {
            return request.getParameter(KEYWORD_PARAMETER_KEY);
        }
        return "";
    }

    /**
     * 요청 파라미터에 카테고리 번호가 있는 경우 그 값을, 아니라면 기본 번호(0)을 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 카테고리 번호가 있는 경우 그 값을, 아니라면 기본 번호(0)을 반환
     */
    public static int getParamCategoryIdx(HttpServletRequest request) {
        if (request.getParameter(CATEGORY_IDX_PARAMETER_KEY) != null) {
            return Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_KEY));
        }
        return 0;
    }
}
