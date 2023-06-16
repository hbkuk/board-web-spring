package com.study.ebsoft.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * API 예외 응답을 나타내는 클래스입니다.
 */
@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String detail;

    /**
     * ErrorResponse 생성자입니다.
     *
     * @param errorCode 예외 코드
     */
    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
