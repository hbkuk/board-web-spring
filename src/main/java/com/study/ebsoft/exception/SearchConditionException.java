package com.study.ebsoft.exception;

/**
 * 검색조건(Search Condition)과 관련된 오류가 발생했을 때 던져지는 Unchecked Exception 입니다.
 */
public class SearchConditionException extends RuntimeException {

    public SearchConditionException(String message) {
        super(message);
    }
}
