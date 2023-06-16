package com.study.ebsoft.exception;

/**
 * API 예외 코드를 정의한 열거형입니다.
 */
public enum ErrorCode {

    INVALID_SEARCH_CONDITION("COMMON-001", "형식에 맞지 않는 파라미터 전달"),

    INVALID_BOARD_DATA("BOARD-001", "형식에 맞지 않는 데이터 전달");

    private final String code;
    private final String message;

    /**
     * ErrorCode 생성자입니다.
     *
     * @param code    예외 코드
     * @param message 예외 메시지
     */
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 예외 코드를 반환합니다.
     *
     * @return 예외 코드
     */
    public String getCode() {
        return code;
    }

    /**
     * 예외 메시지를 반환합니다.
     *
     * @return 예외 메시지
     */
    public String getMessage() {
        return message;
    }
}
