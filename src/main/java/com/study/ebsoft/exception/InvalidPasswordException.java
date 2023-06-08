package com.study.ebsoft.exception;

/**
 * 잘못된 비밀번호가 전달되었을 때 던져지는 Unchecked Exception 입니다.
 */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
