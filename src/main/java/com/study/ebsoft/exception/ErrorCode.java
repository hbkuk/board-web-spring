package com.study.ebsoft.exception;

// TODO: refactor: Bundle FIle
/**
 * API 예외 코드를 정의한 열거형입니다.
 */
public enum ErrorCode {
    INVALID_PARAM("PARAM-001", "형식에 맞지 않는 파라미터 전달"),

    INVALID_BOARD_DATA("BOARD-001", "형식에 맞지 않는 데이터 전달"),
    BOARD_NOT_FOUND("BOARD-002", "게시물을 찾을 수 없음"),
    INVALID_PASSWORD("BOARD-003", "잘못된 비밀번호"),

    INVALID_REQUEST("REQUEST-001", "잘못된 요청입니다.");

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
